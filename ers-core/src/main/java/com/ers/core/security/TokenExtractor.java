package com.ers.core.security;

import com.ers.core.util.ApplicationPropertiesConstants;
import com.ers.core.util.ApplicationPropertiesUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Knows how to extract the token from an HTTP request.
 * 
 * @author avillalobos
 */
@Component
public class TokenExtractor {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenExtractor.class);
    
    @Autowired
    private ApplicationPropertiesUtil appProperties;

    private volatile String tokenCookieName;
    
    /**
     * Pulls the token String out of a HttpServletRequest.
     * 
     * @param request
     * @return 
     */
    public String getToken(HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        return token;
    }

    /**
     * Gets the SecurityToken out of the Cookie.
     * @param request
     * @return 
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie tokenCookie = getTokenCookie(request);

        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return null;
    }
    
    /**
     * Gets the Cookie.
     * 
     * @param request
     * @return 
     */
    private Cookie getTokenCookie(HttpServletRequest request) {
        
        // It's important to make this call first before doing anything else. 
        establishTokenCookieName();

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            LOGGER.warn("The request has no cookies in it! Looked for {} cookie", tokenCookieName);
            return null;
        }

        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), tokenCookieName)) {
                LOGGER.info("Found token cookie: {}. Received cookies={}", tokenCookieName, getCookieNames(cookies));
                return cookie;
            }
        }

        // Cookie not found
        LOGGER.warn("The names of received cookies are: {}, but no {} was found. uri={}", getCookieNames(cookies), tokenCookieName, request.getRequestURI());

        return null;
    }
    
    /**
     * Establishes lazily the name of the token cookie.
     * The name is loaded from the configuration file.
     */
    private void establishTokenCookieName() {
        if (tokenCookieName != null) {
            // Already set, bail out.
            return;
        }

        tokenCookieName = appProperties.getProperty(ApplicationPropertiesConstants.TOKEN_COOKIE_NAME_PROP_NAME);

        LOGGER.info("The token cookie name is set to {}", tokenCookieName);
    }
    
    private static List<String> getCookieNames(Cookie[] cookies) {
        return Arrays.stream(cookies).map(Cookie::getName).collect(Collectors.toList());
    }

}
