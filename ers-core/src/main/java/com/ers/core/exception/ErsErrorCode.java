/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 *
 * @author avillalobos
 */
public enum ErsErrorCode {
    
    GENERAL(1000),
    ACCES_DENIED(1001),
    INVALID_TOKEN(1002),
    INCORRECT_URL(1003),
    DATABASE_ERROR(1004),
    PARAMETER_MISSING(1005),
    TARGET_NOT_EXISTS(1006),
    USER_EMAIL_ALREADY_REGISTERED(1007),
    USER_INFORMATION_MISSING(1008),
    INVALID_ATTRIBUTE_LENGTH(1009),
    INVALID_EMAIL(1010),
    INVALID_PASSWORD_FORMAT(1011),
    INVALID_CREDENTIALS(1012),
    USER_STATUS_DISABLED(1013),
    USER_STATUS_LOCKED(1014);

    private final int code;

    ErsErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
