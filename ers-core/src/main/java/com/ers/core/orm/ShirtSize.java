/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author avillalobos
 */
public enum ShirtSize {

    XS, S, M, L, XL, XL2;
        
    public static ShirtSize getByName(String name) {
        for(ShirtSize shirtSize : values()) {
            if (StringUtils.equalsIgnoreCase(name, shirtSize.name())) {
                return shirtSize;
            }
        }
        return null;
    }
}
