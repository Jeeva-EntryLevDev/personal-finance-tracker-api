package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request, User user);

    List<ExpenseResponse> getExpenses(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

    List<ExpenseResponse> getExpenses(
            Long categoryId,
            User user
    );

    ExpenseResponse updateExpense(
            Long expenseId,
            ExpenseRequest request,
            User user
    );

    void deleteExpense(Long expenseId, User user);
}
