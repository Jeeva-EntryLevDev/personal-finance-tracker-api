package com.jeeva.financetracker.expensetrackerapi.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}

/*
Use when:
    User tries to access another user’s data
    Ownership violations
*/