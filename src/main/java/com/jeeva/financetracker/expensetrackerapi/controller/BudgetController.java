package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.BudgetService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public BudgetResponse createBudget(
            @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User user
    ) {
        return budgetService.createBudget(request, user);
    }

    @GetMapping
    public List<BudgetResponse> getBudgets(
            @RequestParam int month,
            @RequestParam int year,
            @AuthenticationPrincipal User user
    ) {
        return budgetService.getBudgets(user, month, year);
    }

    @PutMapping("/{id}")
    public BudgetResponse updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User user
    ) {
        return budgetService.updateBudget(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void deleteBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        budgetService.deleteBudget(id, user);
    }
}