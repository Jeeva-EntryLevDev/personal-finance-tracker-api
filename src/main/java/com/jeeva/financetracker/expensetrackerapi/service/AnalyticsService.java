package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.analytics.*;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.util.List;

public interface AnalyticsService {

    MonthlySummaryResponse getMonthlySummary(User user, int month, int year);

    List<CategoryExpenseResponse> getExpenseBreakdown(User user, int month, int year);

    List<BudgetStatusResponse> getBudgetStatus(User user, int month, int year);

}