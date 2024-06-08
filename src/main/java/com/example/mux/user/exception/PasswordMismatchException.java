package com.example.mux.user.exception;

public class PasswordMismatchException extends Exception{
    public PasswordMismatchException() {
        super();
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }
}
