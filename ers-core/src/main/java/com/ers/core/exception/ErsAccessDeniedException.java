/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 *
 * @author avillalobos
 */
public class ErsAccessDeniedException extends ErsException {
    
    public ErsAccessDeniedException(String message) {
        super(message, ErsErrorCode.ACCES_DENIED);
    }

}
