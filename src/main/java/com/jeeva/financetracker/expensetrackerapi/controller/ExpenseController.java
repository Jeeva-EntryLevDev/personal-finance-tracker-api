package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponse createExpense(
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User user
    ) {
        return expenseService.createExpense(request, user);
    }

    @GetMapping
    public List<ExpenseResponse> getExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return expenseService.getExpenses(user, startDate, endDate);
    }

    @GetMapping(params = "categoryId")
    public List<ExpenseResponse> getExpensesByCategory(
            @RequestParam Long categoryId,
            @AuthenticationPrincipal User user
    ) {
        return expenseService.getExpenses(categoryId, user);
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User user
    ) {
        return expenseService.updateExpense(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        expenseService.deleteExpense(id, user);
    }
}
