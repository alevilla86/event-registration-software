/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.SecurityTokenDao;
import com.ers.core.dto.LoginRequestDto;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.exception.VerifyUserPasswordException;
import com.ers.core.orm.SecurityToken;
import com.ers.core.orm.User;
import com.ers.core.security.TokenExtractor;
import com.ers.core.security.config.SecurityConstants;
import com.ers.core.util.ApplicationPropertiesConstants;
import com.ers.core.util.ApplicationPropertiesUtil;
import com.ers.core.util.RandomUtils;
import com.ers.core.util.SecurityUtils;
import com.ers.core.util.WebUtils;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author avillalobos
 */
@Service
public class SecurityService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    
    @Autowired
    private SecurityTokenDao securityTokenDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationPropertiesUtil appPropertiesUtil;
    
    @Autowired
    private TokenExtractor tokenExtractor;
    
    /**
     * Gets a SecurityToken by its id.
     * 
     * @param tokenId
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public SecurityToken getSecurityTokenById(String tokenId) throws ErsException {
        return securityTokenDao.getById(tokenId);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public User deleteToken(String tokenId) throws ErsException {

        SecurityToken token = securityTokenDao.getById(tokenId);

        if (token == null) {
            return null;
        }

        securityTokenDao.delete(tokenId);

        return token.getUser();
    }
    
    /**
     * Verifies if a token is valid.
     * Makes the token-touch if necessary.
     * 
     * @param tokenId
     * @param clientIpAddress
     * @param skipTokenTouch
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean isTokenValid(String tokenId, String clientIpAddress, boolean skipTokenTouch) throws ErsException {

        if (StringUtils.isBlank(tokenId) || StringUtils.isBlank(clientIpAddress)) {
            LOGGER.warn("Empty token [{}] or IP address [{}]", tokenId, clientIpAddress);
            return false;
        }

        SecurityToken securityToken = securityTokenDao.getById(tokenId);

        if (securityToken == null) {
            LOGGER.warn("{} token is not valid: not found in DB", tokenId);
            return false;
        }
        
        if (isTokenExpired(securityToken)) {
            LOGGER.warn("{} token is expired.", tokenId);

            // Delete by ID, not using the persistent object because another thread might delete it at the same time.
            // If we delete the persistent instance and is not found in DB, hibernate throws an exception:
            // org.hibernate.StaleStateException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1
            deleteToken(tokenId);
            return false;
        }

        //Make sure the token is used from the same client/host.
        String clientIpAddressInDb = securityToken.getHostIp();

        if (!StringUtils.equals(clientIpAddressInDb, clientIpAddress)) {
            LOGGER.warn("{} token's IP address [{}] is not matching the value in DB: {}", tokenId, clientIpAddress, clientIpAddressInDb);
            return false;
        }

        User loggedUser = securityToken.getUser();

        //If the user is now disabled for any reason the token is no loger valid.
        if (loggedUser.getStatus() == User.Status.DISABLED) {
            LOGGER.warn("Removing {} token because the user status is {}", tokenId, loggedUser.getStatus());

            deleteToken(tokenId);
            return false;
        }

        if (loggedUser.getStatus() == User.Status.LOCKED) {
            //
            // Do nothing for Status.LOCKED at this time.
            //
            // The thought is a malicious user could run a targeted denial of service 
            // against a specific user by trying to login with an incorrect password.
            // If we check for this specific status, then the current legitimate logged in user will be kicked out. 
        }

        if (skipTokenTouch) {
            // Some requests from the client indicate to not update the token last access time.
            // Typically this is used for requests that happen periodically and that aren't initiated by any user actions,
            // otherwise the current token doesn't expire while the browser keeps running.
            LOGGER.info("Token of {} is not touched, IP address={}", loggedUser.getEmail(), clientIpAddress);
        } else {
            touchSecurityToken(securityToken);
        }

        return true;
    }
    
    /**
     * Verifies if a token hasn't expired.
     * 
     * @param securityToken
     * @return 
     */
    private boolean isTokenExpired(SecurityToken securityToken) {
        long lastAccessTime = securityToken.getDateLastAccess().getTime();
        long now = System.currentTimeMillis();

        boolean expired = now - lastAccessTime > getTokenTimeout();

        return expired;
    }

    /**
     * For how long is the token valid.
     * 
     * @return 
     */
    private long getTokenTimeout() {

        long timeoutInMinutes = appPropertiesUtil.getLongProperty(ApplicationPropertiesConstants.TOKEN_TIMEOUT_IN_MINUTES_PROP_NAME);
        LOGGER.info("Token timeout is set to {} minutes", timeoutInMinutes);

        long tokenTimeoutInMilliseconds = TimeUnit.MINUTES.toMillis(timeoutInMinutes);

        return tokenTimeoutInMilliseconds;
    }
    
    public void touchSecurityToken(SecurityToken securityToken) throws ErsException {
        Date now = new Date();
        securityToken.setDateLastAccess(now);
        securityTokenDao.update(securityToken);
    }
    
    /**
     * Verify the user credentials and logs him in (creates a token).
     * Must not rollback for VerifyUserPasswordException since we save in the DB
     * the number of failed attempts.
     * 
     * @param loginRequest
     * @param request
     * @return
     * @throws ErsException 
     */
    @Transactional(noRollbackFor = VerifyUserPasswordException.class)
    public User login(LoginRequestDto loginRequest, HttpServletRequest request) throws ErsException {
        
        String userEmail = loginRequest.getEmail();
        User user;
            
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (ErsException ex) {
            throw new ErsException("Invalid credentials", ErsErrorCode.INVALID_CREDENTIALS);
        }
        
        User.Status userStatus = user.getStatus();

        if (userStatus == User.Status.DISABLED) {
            LOGGER.error("{} user status is {} and is not allowed to authenticate", user.getEmail(), userStatus);
            throw new ErsException("Account disabled", ErsErrorCode.USER_STATUS_DISABLED);
        }

        if (userStatus == User.Status.LOCKED) {
            LOGGER.error("{} user status is {} and is not allowed to authenticate", user.getEmail(), userStatus);
            throw new ErsException("Account locked", ErsErrorCode.USER_STATUS_LOCKED);
        }
        
        verifyUserPassword(user, loginRequest.getPassword());
        
        // The token is only valid for one IP address.
        String ipAddress = WebUtils.getClientIpAddress(request);
        
        // Create a new security token
        SecurityToken newToken = saveNewSecurityToken(user, ipAddress);

        user.setTokenInUse(newToken);

        LOGGER.info("User {} authenticated successfully.", user.getEmail());
        
        return user;
    }
    
    /**
     * Verifies a user password.
     * Note this method has side effects: updates the DB.
     * 
     * @param existingUser
     * @param password
     * @throws VerifyUserPasswordException This exception should be configured to not roll back the transaction.
     */
    private void verifyUserPassword(User existingUser, String password) throws VerifyUserPasswordException, ErsException {

        boolean passwordMatched = isPasswordMatching(existingUser, password);

        if (!passwordMatched) {
            
            int currentTries = existingUser.getFailedLoginAttempts();
            int maxTries = appPropertiesUtil.getIntProperty(ApplicationPropertiesConstants.SECURITY_LOGIN_MAX_FAILED_ATTEMPTS);

            if (currentTries >= maxTries) {
                LOGGER.error("User [{}] tried to authenticate more than [{}] times and has been locked.", existingUser.getEmail(), maxTries);

                // Updating the user status
                existingUser.setStatus(User.Status.LOCKED);
                userService.updateUser(existingUser);

                // returning the error
                throw new VerifyUserPasswordException("User has been locked.", ErsErrorCode.USER_STATUS_LOCKED);
            }

            // Increase the password failed attempts
            existingUser.setFailedLoginAttempts(++currentTries);

            // Updating the user failed attempts
            userService.updateUser(existingUser);

            LOGGER.error("User [{}] password did not match", existingUser.getEmail());

            // Note the DB transaction is not rolled back for this specific exception class.
            // We just increased the number of failed login attempts and we want to save that into DB.
            // See spring-persistence.xml
            throw new VerifyUserPasswordException("Invalid credentials", ErsErrorCode.INVALID_CREDENTIALS);
        }

        // If the user failed before and got the password right we need to reset the failed attempts
        existingUser.setFailedLoginAttempts(0);
        userService.updateUser(existingUser);

    }
    
     /**
     * Verifies if a hashed password matches a plain text.
     * 
     * @param existingUser
     * @param password
     * @return 
     */
    private boolean isPasswordMatching(User existingUser, String password) {
        boolean matched = SecurityUtils.checkBcrypt(password, existingUser.getPassword());
        return matched;
    }
    
    /**
     * Saves the new user token in the database after a successful login.
     * 
     * @param user
     * @param hostIp
     * @return
     * @throws ErsException 
     */
    private SecurityToken saveNewSecurityToken(User user, String hostIp) throws ErsException {
        
        Date date = new Date();

        SecurityToken newToken = new SecurityToken();
        newToken.setUser(user);
        newToken.setHostIp(hostIp);
        newToken.setDateCreated(date);
        newToken.setDateLastAccess(date);

        String tokenId = RandomUtils.randomNumericString(SecurityConstants.SECURITY_TOKEN_LENGTH);
        newToken.setId(tokenId);

        securityTokenDao.save(newToken);
        
        return newToken;
    }
    
    /**
     * Logs out an user.
     * 
     * @param request
     * @param response 
     */
    @Transactional(rollbackFor = Exception.class)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            String tokenId = tokenExtractor.getToken(request);

            if (StringUtils.isBlank(tokenId)) {
                LOGGER.warn("Trying to invalidate a token with an empty tokenId");

                // Return success all the time.
                return;
            }

            User loggedUser = deleteToken(tokenId);

            LOGGER.info("Deleted token {}: {}", tokenId, loggedUser != null);

        } catch (ErsException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
