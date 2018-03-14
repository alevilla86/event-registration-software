/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 *
 * @author avillalobos
 */
// Exception used exclusively by SecurityService::verifyUserPassword()
// It's configured to not roll back transactions because we keep in DB a count 
// of failed authentication attempts.
public class VerifyUserPasswordException extends ErsException {


    public VerifyUserPasswordException(String message, ErsErrorCode errorCode) {
        super(message, errorCode);
    }
}
