package com.ers.core.security.config;

/**
 *
 * @author avillalobos
 */
public interface SecurityConstants {
    
    /** Logged user when token validator is disabled. */
    public static final String DEFAULT_USER_ID = "1";
    
    /** Name of the token to authenticate users. */
    //public static final String AUTH_TOKEN_HEADER_NAME = "ers-token";
    
    /** Parameter in the request to avoid to touch the token. */
    public static final String SKIP_TOKEN_TOUCH_QUERY_PARAM_NAME = "skip-token-touch";
    
    /** Header name for X-Forwarded-For */
    public static final String X_FORWARDED_FOR_HEADER_NAME = "X-Forwarded-For";
    
    public static final int SECURITY_TOKEN_LENGTH = 32;
    
}
