package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.util.List;

public interface BudgetService {

    BudgetResponse createBudget(BudgetRequest request, User user);

    List<BudgetResponse> getBudgets(
            User user,
            int month,
            int year
    );

    BudgetResponse updateBudget(
            Long budgetId,
            BudgetRequest request,
            User user
    );

    void deleteBudget(Long budgetId, User user);
}