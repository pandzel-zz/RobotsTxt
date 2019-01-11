package com.panforge.robotstxt.exception;

/**
 * Failure to Execute query
 * @author vishnu rao
 */
public class QueryExecutionException extends Exception {

    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
