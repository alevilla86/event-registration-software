/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author avillalobos
 */
public enum Currency {
        CRC, USD;
        
        public static Currency getByName(String name) {
            for (Currency currency : values()) {
                if (StringUtils.equals(currency.name(), name)) {
                    return currency;
                }
            }
            
            return null;
        }
    }
