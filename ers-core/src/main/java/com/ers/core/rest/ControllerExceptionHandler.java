/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest;

import com.ers.core.exception.ErsAccessDeniedException;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.exception.ExceptionViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author avillalobos
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    
    public static final String GENERAL_ERROR_MSG = "general error";
    
    private ErsException logCodeAndMessage(ErsException ex) {

        log.error("error=" + ex.getErrorCode() + ", code=" + ex.getErrorCodeInt() + ", message=" + ex.getMessage());
        return ex;
    }
    
    @JsonView(ExceptionViews.Summary.class)
    @ExceptionHandler(ErsException.class)
    @ResponseBody
    public ErsException handleErsException(ErsException ex) {
        
        return logCodeAndMessage(ex);
    }
    
    @JsonView(ExceptionViews.Summary.class)
    @ExceptionHandler(ErsAccessDeniedException.class)
    @ResponseBody
    public ErsException handleAuthorizationException(ErsAccessDeniedException ex) {
        
        log.error("Authorization exception", ex);
        return logCodeAndMessage(ex);
    }
    
    @JsonView(ExceptionViews.Summary.class)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErsException handleAllExceptions(Exception ex) {
        
        log.error(ex.getMessage(), ex);
        return logCodeAndMessage(new ErsException(GENERAL_ERROR_MSG, ErsErrorCode.GENERAL));
    }
    
    @JsonView(ExceptionViews.Summary.class)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ErsException handleNoHandlerFoundException(NoHandlerFoundException e) {

        log.error(e.getMessage(), e);
        return logCodeAndMessage(new ErsException("Incorrect URL", ErsErrorCode.INCORRECT_URL));
    }

}
