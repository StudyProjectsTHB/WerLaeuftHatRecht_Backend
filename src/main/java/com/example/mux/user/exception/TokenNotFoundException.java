package com.example.mux.user.exception;

import com.example.mux.exception.EntityNotFoundException;

public class TokenNotFoundException extends EntityNotFoundException {
    public TokenNotFoundException() {
        super();
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }
}
