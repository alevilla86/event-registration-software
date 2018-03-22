/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 *
 * @author avillalobos
 */
public enum ErsErrorCode {
    
    /*
    Platform error codes. 1000.
    */
    GENERAL(1000),
    ACCES_DENIED(1001),
    INCORRECT_URL(1002),
    DATABASE_ERROR(1003),
    IO_EXCEPTION(1004),
    
    /*
    General entity error codes. 1100.
    */
    PARAMETER_MISSING(1101),
    TARGET_NOT_EXISTS(1102),
    INVALID_ATTRIBUTE_LENGTH(1103),
    INVALID_EMAIL(1104),
    INVALID_DATE(1105),
    INVALID_PASSWORD_FORMAT(1106),
    INVALID_PARAMETER(1107),
    
    /*
    User registration error codes. 1200.
    */
    USER_EMAIL_ALREADY_REGISTERED(1201),
    
    /*
    User login error codes. 1300.
    */
    INVALID_CREDENTIALS(1301),
    INVALID_TOKEN(1302),
    USER_STATUS_DISABLED(1303),
    USER_STATUS_LOCKED(1304),
    
    /*
    User profile error codes. 1400.
    */
    MISSING_INFORMATION_USER(1401),
    MISSING_INFORMATION_USER_PROFILE(1402),
    NOT_FOUND_USER_PROFILE(1403),
    NOT_FOUND_USER(1404),
    
    /*
    Picture error codes. 1500
    */
    INVALID_PICTURE_EXTENSION(1501),
    INVALID_PICTURE_FORMAT(1502),
    PICTURE_DAO_NOT_INITIALIZED(1503),
    
    /*
    Category and Role error codes. 1600
    */
    NOT_FOUND_CATEGORY(1601),
    NOT_FOUND_ROLE(1602),
    
    /*
    Event error codes. 1700
    */
    MISSING_INFORMATION_EVENT(1701),
    NOT_FOUND_EVENT(1702),
    EVENT_HAS_USERS_REGISTERED(1703),
    EVENT_HAS_NO_REGISTRATION_OPTIONS(1704),
    NOT_FOUND_EVENT_OPTION(1705),
    EVENT_REGISTRATION_OPTION_CORRESPONDS_TO_OTHER_EVENT(1706),
    EVENT_REGISTRATION_DATE_EXPIRED(1707),
    MISSING_INFORMATION_USER_EVENT_REGISTRATION(1708),
    MISSING_INFORMATION_EVENT_REGISTRATION_OPTION(1709),
    ;

    private final int code;

    ErsErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
