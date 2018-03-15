/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest.controller;

import com.ers.core.dto.LoginRequestDto;
import com.ers.core.dto.UserCreateDto;
import com.ers.core.dto.UserDto;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.SecurityToken;
import com.ers.core.orm.User;
import com.ers.core.service.SecurityService;
import com.ers.core.service.UserService;
import com.ers.core.constants.ApplicationPropertiesConstants;
import com.ers.core.util.ApplicationPropertiesUtil;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author avillalobos
 */
@RestController
public class UserController {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private ApplicationPropertiesUtil appPropertiesUtil;
    
    @PostMapping("/users/create")
    public UserDto createUser(@RequestBody UserCreateDto user) throws ErsException {
        
        LOGGER.info("User account is being created [userDto={}]", user);
        
        UserDto result = userService.createUser(user);
        
        return result;
    }
    
    @PostMapping("/users/login")
    public boolean loginUser(HttpServletRequest httpRequest, 
            HttpServletResponse httpResponse,
            @RequestBody LoginRequestDto loginRequestDto) throws ErsException {
        
        LOGGER.info("{} is logging in", loginRequestDto.getEmail());
        
        User loggedUser = securityService.login(loginRequestDto, httpRequest);
        
        addTokenCookieToResponse(loggedUser.getTokenInUse(), httpResponse);
        
        LOGGER.info("{} has logged in", loginRequestDto.getEmail());
        
        return true;
    }
    
    /**
     * Non-secured call to log out users by removing token cookies.
     * 
     * @param httpRequest
     * @param httpResponse
     * @return 
     */
    @PostMapping(value = "/users/logout")
    public boolean logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        securityService.logout(httpRequest, httpResponse);
        
        removeTokenCookie(httpResponse);

        //Return success all the time.
        return true;
    }
    
    /**
     * Adds the token cookie to the response.
     * 
     * @param securityToken
     * @param response 
     */
    private void addTokenCookieToResponse(SecurityToken securityToken, HttpServletResponse response) {
        Cookie cookie = createTokenCookie(securityToken.getId());
        response.addCookie(cookie);
        LOGGER.info("Added {} cookie to response; domain={}, secure={} ", cookie.getName(), cookie.getDomain(), cookie.getSecure());
    }

    /**
     * Creates a Cookie using the application properties.
     * 
     * @param value
     * @return 
     */
    private Cookie createTokenCookie(String value) {
        String cookieName = appPropertiesUtil.getProperty(ApplicationPropertiesConstants.TOKEN_COOKIE_NAME_PROP_NAME);
        String domain = appPropertiesUtil.getProperty(ApplicationPropertiesConstants.TOKEN_COOKIE_DOMAIN_PROP_NAME);
        boolean secure = appPropertiesUtil.getBooleanProperty(ApplicationPropertiesConstants.TOKEN_COOKIE_SECURE_PROP_NAME);

        Cookie cookie = new Cookie(cookieName, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);

        // Use a negative value to indicate the cookie is not stored persistently 
        // and should be deleted when the Web browser exits
        cookie.setMaxAge(-1);

        return cookie;
    }
    
    /**
     * Removes the token cookie.
     * 
     * @param httpResponse 
     */
    private void removeTokenCookie(HttpServletResponse httpResponse) {
        Cookie tokenCookie = createTokenCookie(null);
        tokenCookie.setMaxAge(0); // Deletes the token cookie
        httpResponse.addCookie(tokenCookie);
    }

}