package com.jeeva.financetracker.expensetrackerapi.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}


/*
Use when:
    Wrong category type - if it's not it's shows 403 fobidden error
    Invalid input
    Business rule violation
*/