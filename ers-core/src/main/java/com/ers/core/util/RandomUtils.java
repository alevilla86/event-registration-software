/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.util;

import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author avillalobos
 */
public class RandomUtils {
    
    // Note this is a secure random.
    private static final Random RANDOM = new SecureRandom();

    /**
     * Private constructor to prevent instantiations.
     */
    private RandomUtils() {
    }

    /**
     * Creates a random string whose length is the number of characters specified.
     *
     * Characters will be chosen from the set of numeric characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomNumericString(int count) {
        // The default random object used by RandomStringUtils is not secure.
        // We must use this method so we can pass a secure random.
        return RandomStringUtils.random(count, 0, 0, false, true, null, RANDOM);
    }

    /**
     * Creates a random string whose length is the number of characters specified.
     *
     * Characters will be chosen from the set of alpha-numeric characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphanumericString(int count) {
        // The default random object used by RandomStringUtils is not secure.
        // We must use this method so we can pass a secure random.
        return RandomStringUtils.random(count, 0, 0, true, true, null, RANDOM);
    }

}
