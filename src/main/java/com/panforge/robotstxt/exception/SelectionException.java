package com.panforge.robotstxt.exception;

/**
 * unable to select.
 * @author vishnu rao
 */
public class SelectionException extends Exception{
    public SelectionException(String message) {
        super(message);
    }

    public SelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
