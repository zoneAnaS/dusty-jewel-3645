package com.sweetopia.exception;

public class SessionsException extends Exception {
    public SessionsException() {
    }

    public SessionsException(String message) {
        super(message);
    }

    public SessionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionsException(Throwable cause) {
        super(cause);
    }
}
