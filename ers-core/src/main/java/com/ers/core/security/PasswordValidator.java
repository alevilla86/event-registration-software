/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.security;

import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.util.ApplicationPropertiesConstants;
import com.ers.core.util.ApplicationPropertiesUtil;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author avillalobos
 */
@Component
public class PasswordValidator {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(PasswordValidator.class);
    
    @Autowired
    private ApplicationPropertiesUtil appProperties;
    
    /**
     * Validates the password complies the requirements.
     * 
     * @param password
     * @param userEmail
     * @throws ErsException 
     */
    public void validate(String password, String userEmail) throws ErsException {
        
        boolean passwordValidatorEnabled = appProperties.getBooleanProperty(ApplicationPropertiesConstants.SECURITY_PASSWORD_VALIDATOR_ENABLED);
        
        if (!passwordValidatorEnabled) {
            LOGGER.warn("Password validation is disabled");
            return;
        }
        
        if (StringUtils.isBlank(password)) {
            LOGGER.error("{} password can not be empty", userEmail);
            throw new ErsException("Invalid password format", ErsErrorCode.INVALID_PASSWORD_FORMAT);
        }

        int minPasswordLength = appProperties.getIntProperty(ApplicationPropertiesConstants.USER_MIN_PASSWORD_LENGTH_PROP_NAME);
        if (password.length() < minPasswordLength) {
            LOGGER.error("{} password must have at least {} characters", userEmail, minPasswordLength);
            throw new ErsException("Invalid password format", ErsErrorCode.INVALID_PASSWORD_FORMAT);
        }

        if (!isDiverse(password)) {
            LOGGER.error("{} password must have at least 1 digit, 1 uppercase letter, 1 lowercase letter, 1 special character", userEmail);
            throw new ErsException("Invalid password format", ErsErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    /**
     * Check for at least 1 digit, 1 uppercase letter, 1 lowercase letter, 1 special character.
     * Does not check for length
     * 
     * @param password
     * @return true if the password matches, false otherwise
     */
    private boolean isDiverse(String password) {
        Pattern matchpattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*\\d)(?=.*?[^\\w\\s]).{4,}");
        return matchpattern.matcher(password).matches();
    }

}
