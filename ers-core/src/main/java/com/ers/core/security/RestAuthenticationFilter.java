/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.security;

import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.SecurityToken;
import com.ers.core.orm.User;
import com.ers.core.security.config.SecurityConstants;
import com.ers.core.service.SecurityService;
import com.ers.core.service.UserService;
import com.ers.core.util.WebUtils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

/**
 * Filter to all request, decides what request is nonSecured (based of configuration
 * SecurityConfiguration) and attempts to read a token from the requests to all
 * other requests that are secured.
 * If the token is present querys the database to identify the user that is doing
 * the request, to do that the token needs to exist and be still valid (not expired)
 * Once the user is identified with the token, this user is set to the security 
 * context.
 * 
 * @see SecurityConfiguration
 * @author avillalobos
 */
public class RestAuthenticationFilter extends GenericFilterBean {
    
    /** Logger instance. */
    private final static Logger log = LoggerFactory.getLogger(RestAuthenticationFilter.class);
    
    /** Paths that not requires authentication. */
    private String[] nonSecuredPath;
    
    /** Should check for token on secured requests. */
    private boolean tokenValidatorEnabled = true;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private TokenExtractor tokenExtractor;
    
    public RestAuthenticationFilter() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String resourcePath = new UrlPathHelper().getPathWithinApplication(request);
        log.info("Request received with resourcePath={}", resourcePath);
        
        //Nothing else to do here.
        if (!requiresAuthentication(resourcePath)) {
            chain.doFilter(req, res);
            return;
        }
        
        try {
            //Authentication is required.
            attemptAuthentication(request, response);
            chain.doFilter(req, res);
        } catch (ErsException ex) {
            onUnsuccessfulAuthentication(request, response, new AuthenticationServiceException(ex.getMessage(), ex));
        }
    }
    
    /**
     * Verifies if the path is non secured and if the token validator is enabled.
     * 
     * @param resourcePath
     * @return 
     */
    private boolean requiresAuthentication(String resourcePath) {
        
        if (StringUtils.isBlank(resourcePath)) {
            log.info("Path is not secured because it's missing");
            return false;
        }
        
        for (String nonSecured : nonSecuredPath) {
            if (StringUtils.startsWithIgnoreCase(resourcePath, nonSecured)) {
                log.info("Path is not secured, no user needs to be logged. Path={}", resourcePath);
                return false;
            }
        }
        
        //If we are here this request needs to be authenticated.
        //However, we check if we have the token validator enabled first.
        if (tokenValidatorEnabled) {
            return true;
        }
        
        //The request should be secured, however we are using a default user.
        addDefaultUserInContext();
        return false;
    }
    
    /**
     * Adds a default user when token validator is disabled.
     * 
     */
    private void addDefaultUserInContext() {
        
        String userId = SecurityConstants.DEFAULT_USER_ID;
        log.warn("Path is secured but token Validator DISABLED. Login using user: {}", userId);

        User user;
        try {
            user = userService.getUserById(userId);
        } catch (ErsException e) {
            log.error("Error loading the default user with {} ID", userId, e);
            throw new IllegalStateException(e.getMessage(), e);
        }

        if (user == null) {
            throw new IllegalStateException("Default user with " + userId + " ID is not found in DB.");
        }

        // Set a dummy token in use (doesn't exist in DB).
        user.setTokenInUse(new SecurityToken());

        Authentication authentication = new RunAsUserToken(user.getId(), user, null, Collections.emptyList(), null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    /**
     * Authenticates an user using the token.
     * 
     * @param request
     * @param response
     * @return
     * @throws ErsException 
     */
    private void attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ErsException {
        
        log.info("Path is secured. Token validator is ENABLED. A token MUST be present in this request.");
        
        String token = tokenExtractor.getToken(request);
        
        if (StringUtils.isBlank(token)) {
            throw new ErsException("Token is not present in the request", ErsErrorCode.INVALID_TOKEN);
        }
        
        SecurityToken userToken = securityService.getSecurityTokenById(token);
        
        String clientIpAddress = WebUtils.getClientIpAddress(request);
        boolean skipTokenTouch = getSkipTokenTouchQueryParam(request);
        
        //Now that we have the token, make sure it's valid.
        securityService.isTokenValid(userToken.getId(), clientIpAddress, skipTokenTouch);
        
        User loggedUser = userToken.getUser();
        loggedUser.setTokenInUse(userToken);
        
        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();
        Authentication authentication = new RunAsUserToken(loggedUser.getId(), loggedUser, null, authorities, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setNonSecuredPath(String[] nonSecuredPath) {
        this.nonSecuredPath = nonSecuredPath;
    }

    public void setTokenValidatorEnabled(boolean tokenValidatorEnabled) {
        this.tokenValidatorEnabled = tokenValidatorEnabled;
        
        if (!tokenValidatorEnabled) {
            log.warn("Token validator has been disabled");
        }
    }
    
    /**
     * When an authorization attempt fails.
     * 
     * @param request
     * @param response
     * @param ex
     * @throws IOException 
     */
    private void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        
        log.debug("User authentication failure");

        response.setContentType("application/json;charset=UTF-8");

        try (OutputStream out = response.getOutputStream()) {
            ErsErrorCode errorCode;

            if (ex.getCause() instanceof ErsException) {
                ErsException cause = (ErsException) ex.getCause();
                errorCode = cause.getErrorCode();
            } else {
                errorCode = ErsErrorCode.INVALID_TOKEN;
            }

            JsonObject json = new JsonObject();
            json.addProperty("message", ex.getMessage());
            json.addProperty("errorCode", errorCode.getCode());

            String jsonString = json.toString();

            log.info("Response sent to client: {}", jsonString);
            out.write(jsonString.getBytes());
        }
    }
    
    /**
     * Whether token should be touched or not.
     * 
     * @param request
     * @return 
     */
    private boolean getSkipTokenTouchQueryParam(HttpServletRequest request) {

        String value = request.getParameter(SecurityConstants.SKIP_TOKEN_TOUCH_QUERY_PARAM_NAME);
        return Boolean.parseBoolean(value);
    }

}
