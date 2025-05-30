package com.sqzer.hsbctransaction.exception;

public class CustomRateLimitException extends RuntimeException {
    public CustomRateLimitException(String message) {
        super(message);
    }
}
