package com.ers.core.util;

/**
 *
 * @author avillalobos
 */
public interface ApplicationPropertiesConstants {
    /*
    SECURITY PROPERTIES KEYS
    */
    public static final String SECURITY_TOKEN_VALIDATOR_ENABLED = "security.token.validator.enabled";
    public static final String SECURITY_PASSWORD_VALIDATOR_ENABLED = "security.password.validator.enabled";
    public static final String USER_MIN_PASSWORD_LENGTH_PROP_NAME = "security.user.min.password.length";
    public static final String SECURITY_LOGIN_MAX_FAILED_ATTEMPTS = "security.login.max.failed.attempts";
    public static final String TOKEN_COOKIE_NAME_PROP_NAME = "security.token.cookie.name";
    public static final String TOKEN_COOKIE_DOMAIN_PROP_NAME = "security.token.cookie.domain";
    public static final String TOKEN_COOKIE_SECURE_PROP_NAME = "security.token.cookie.secure";
    public static final String TOKEN_TIMEOUT_IN_MINUTES_PROP_NAME = "security.token.timeout";

}
