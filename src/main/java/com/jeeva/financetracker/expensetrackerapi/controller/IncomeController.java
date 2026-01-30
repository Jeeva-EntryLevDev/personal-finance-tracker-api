package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.IncomeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public IncomeResponse createIncome(
            @RequestBody IncomeRequest request,
            @AuthenticationPrincipal User user
    ) {
        return incomeService.createIncome(request, user);
    }

    @GetMapping
    public List<IncomeResponse> getIncomes(
            @AuthenticationPrincipal User user,

            @RequestParam(required = false) Long categoryId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return incomeService.getIncomes(user, categoryId, startDate, endDate);
    }


    @PutMapping("/{id}")
    public IncomeResponse updateIncome(
            @PathVariable Long id,
            @RequestBody IncomeRequest request,
            @AuthenticationPrincipal User user
    ) {
        return incomeService.updateIncome(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void deleteIncome(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        incomeService.deleteIncome(id, user);
    }
}
