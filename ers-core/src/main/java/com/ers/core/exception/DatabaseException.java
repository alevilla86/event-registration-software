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
