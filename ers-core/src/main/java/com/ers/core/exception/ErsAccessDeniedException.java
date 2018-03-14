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
