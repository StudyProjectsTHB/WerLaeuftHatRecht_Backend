package com.example.mux.user.exception;

public class TokenExpiredException extends Exception{
    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
