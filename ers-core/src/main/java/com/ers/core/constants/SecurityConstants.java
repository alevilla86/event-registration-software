/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.constants;

/**
 *
 * @author avillalobos
 */
public interface SecurityConstants {
    
    /** Logged user when token validator is disabled. */
    public static final String DEFAULT_USER_ID = "2";
    
    /** Parameter in the request to avoid to touch the token. */
    public static final String SKIP_TOKEN_TOUCH_QUERY_PARAM_NAME = "skip-token-touch";
    
    /** Header name for X-Forwarded-For */
    public static final String X_FORWARDED_FOR_HEADER_NAME = "X-Forwarded-For";
    
    public static final int SECURITY_TOKEN_LENGTH = 32;
    
}
