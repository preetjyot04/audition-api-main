package com.audition.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    static final long serialVersionUID = 42L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
