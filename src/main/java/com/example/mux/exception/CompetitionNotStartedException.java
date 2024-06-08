package com.example.mux.exception;

public class CompetitionNotStartedException extends Exception {
    public CompetitionNotStartedException() {
        super();
    }

    public CompetitionNotStartedException(String message) {
        super(message);
    }

    public CompetitionNotStartedException(Throwable cause) {
        super(cause);
    }
}
