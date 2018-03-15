/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.config;

import com.ers.core.security.TokenAuthenticationEntryPoint;
import com.ers.core.security.RestAuthenticationFilter;
import com.ers.core.constants.ApplicationPropertiesConstants;
import com.ers.core.util.ApplicationPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration of the web security.
 * Sets the filter that actually logs in uses if needed, based on tokens.
 * Sets the authentication entry point for sending authentication errors.
 * Configures the paths that doesn't needs to be secured.
 * Configures if we are validating via token or if using default user for testing.
 * 
 * @author avillalobos
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private ApplicationPropertiesUtil properties;
        
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        http.addFilterAfter(restAuthenticationFilter(), BasicAuthenticationFilter.class);
        
    }
    
    @Bean
    public RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        
        RestAuthenticationFilter filter = new RestAuthenticationFilter();
        filter.setNonSecuredPath(configureNonSecuredPaths());
        filter.setTokenValidatorEnabled(properties.getBooleanProperty(ApplicationPropertiesConstants.SECURITY_TOKEN_VALIDATOR_ENABLED));
        
        return filter;
    }
    
    @Bean
    public TokenAuthenticationEntryPoint authenticationEntryPoint() {
        
        TokenAuthenticationEntryPoint entry = new TokenAuthenticationEntryPoint();
        return entry;
    }
    
    /**
     * Paths that not requires authentication.
     * 
     * @return 
     */
    private String[] configureNonSecuredPaths() {
        
        String[] nonSecuredPaths = {
            "/tests",
            "/users/create",
            "/users/login",
            "/users/logout"
        };
        
        return nonSecuredPaths;
    }
}
