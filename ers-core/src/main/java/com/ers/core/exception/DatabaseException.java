/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 *
 * @author avillalobos
 */
public class DatabaseException extends ErsException {
    
    public DatabaseException(Throwable cause) {
        super("Database error", ErsErrorCode.DATABASE_ERROR, cause);
    }

}
