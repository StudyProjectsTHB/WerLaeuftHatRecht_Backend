package com.example.mux.exception;

public class DaysNotInCompetitionException extends Exception {
    public DaysNotInCompetitionException() {
        super();
    }

    public DaysNotInCompetitionException(String message) {
        super(message);
    }

    public DaysNotInCompetitionException(Throwable cause) {
        super(cause);
    }
}
