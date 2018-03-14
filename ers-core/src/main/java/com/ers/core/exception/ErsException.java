/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

import com.fasterxml.jackson.annotation.JsonView;

/**
 *
 * @author avillalobos
 */
public class ErsException extends Exception {
    
    private ErsErrorCode errorCode;
    
    public ErsException(String message, ErsErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public ErsException(String message, ErsErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErsException(String message) {
        this(message, ErsErrorCode.GENERAL);
    }

    @JsonView(ExceptionViews.Summary.class)
    public ErsErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErsErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    @JsonView(ExceptionViews.Summary.class)
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    
    @JsonView(ExceptionViews.Summary.class)
    public int getErrorCodeInt() {
        return errorCode.getCode();
    }

}
