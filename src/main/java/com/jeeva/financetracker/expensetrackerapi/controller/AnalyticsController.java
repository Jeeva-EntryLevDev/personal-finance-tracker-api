package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.analytics.*;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.AnalyticsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public MonthlySummaryResponse getSummary(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return analyticsService.getMonthlySummary(user, month, year);
    }

    @GetMapping("/expense-breakdown")
    public List<CategoryExpenseResponse> getExpenseBreakdown(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return analyticsService.getExpenseBreakdown(user, month, year);
    }

    @GetMapping("/budget-status")
    public List<BudgetStatusResponse> getBudgetStatus(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return analyticsService.getBudgetStatus(user, month, year);
    }
}