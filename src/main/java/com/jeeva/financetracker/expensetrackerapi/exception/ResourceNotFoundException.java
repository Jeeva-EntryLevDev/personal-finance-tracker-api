package com.jeeva.financetracker.expensetrackerapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}


/*
Use when:
    Category not found
    Income not found
    Expense not found
 */