/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.constants;

/**
 * Contains the keys of the application-config.properties file.
 * 
 * @author avillalobos
 */
public interface ApplicationPropertiesConstants {
    /*
    SECURITY PROPERTIES KEYS.
    */
    public static final String SECURITY_TOKEN_VALIDATOR_ENABLED = "security.token.validator.enabled";
    public static final String SECURITY_PASSWORD_VALIDATOR_ENABLED = "security.password.validator.enabled";
    public static final String USER_MIN_PASSWORD_LENGTH_PROP_NAME = "security.user.min.password.length";
    public static final String SECURITY_LOGIN_MAX_FAILED_ATTEMPTS = "security.login.max.failed.attempts";
    public static final String TOKEN_COOKIE_NAME_PROP_NAME = "security.token.cookie.name";
    public static final String TOKEN_COOKIE_DOMAIN_PROP_NAME = "security.token.cookie.domain";
    public static final String TOKEN_COOKIE_SECURE_PROP_NAME = "security.token.cookie.secure";
    public static final String TOKEN_TIMEOUT_IN_MINUTES_PROP_NAME = "security.token.timeout";
    public static final String ALLOWED_PICTURE_FILE_EXTENSIONS = "allowed.picture.file.extensions";
    
    /*
    SERVER FILE UPLOAD PROPERTIES KEYS.
    */
    public static final String MAX_UPLOAD_SIZE = "max.upload.size";

}
