package com.panforge.robotstxt.exception;

/**
 * failed to match within specific timeout
 * @author vishnu rao
 */
public class MatchingTimeoutException extends Exception {
    public MatchingTimeoutException(int timeoutMs) {
        super("Failed to do a match within time period of " + timeoutMs + " ms.");
    }
}
