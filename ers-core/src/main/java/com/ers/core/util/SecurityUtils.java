/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author avillalobos
 */
public class SecurityUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

    // The log2 of the number of rounds of hashing to apply.
    // The work factor therefore increases as 2**log_rounds.
    // Minimum 4, maximum 31
    private static final int DEFAULT_LOG2_ROUNDS = 12;
    
    /**
     * Hash a value/password using the OpenBSD bcrypt scheme.
     * 
     * @param value What to hash
     * @return hash result
     */
    public static String getBcryptHash(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt(DEFAULT_LOG2_ROUNDS));
    }
    
    /**
     * Check that a plaintext value matches a previously hashed one
     * 
     * @param plainText the plain text password to verify
     * @param bcryptHash the previously-hashed result of {@link #getBcryptHash(String)}
     * @return true if the passwords match, false otherwise
     */
    public static boolean checkBcrypt(String plainText, String bcryptHash) {
        if (StringUtils.isBlank(bcryptHash)) {
            // The hash can't be null or empty
            return false;
        }

        if (plainText == null) {
            // The value can't be null.
            return false;
        }

        try {
            return BCrypt.checkpw(plainText, bcryptHash);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            return false;
        }
    }
}
