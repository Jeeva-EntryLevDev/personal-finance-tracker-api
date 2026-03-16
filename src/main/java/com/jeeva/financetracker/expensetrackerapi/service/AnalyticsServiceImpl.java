package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.analytics.*;
import com.jeeva.financetracker.expensetrackerapi.entity.*;
import com.jeeva.financetracker.expensetrackerapi.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public AnalyticsServiceImpl(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            BudgetRepository budgetRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public MonthlySummaryResponse getMonthlySummary(User user, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        BigDecimal totalIncome = incomeRepository
                .findByUserAndDateBetween(user, start, end)
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenseRepository
                .findByUserAndDateBetween(user, start, end)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlySummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netSavings(totalIncome.subtract(totalExpense))
                .build();
    }

    @Override
    public List<CategoryExpenseResponse> getExpenseBreakdown(User user, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository
                .findByUserAndDateBetween(user, start, end)
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                e -> e.getCategory().getName(),
                                java.util.stream.Collectors.mapping(
                                        Expense::getAmount,
                                        java.util.stream.Collectors.reducing(
                                                BigDecimal.ZERO,
                                                BigDecimal::add
                                        )
                                )
                        )
                )
                .entrySet()
                .stream()
                .map(entry ->
                        CategoryExpenseResponse.builder()
                                .categoryName(entry.getKey())
                                .totalAmount(entry.getValue())
                                .build()
                )
                .toList();
    }

    @Override
    public List<BudgetStatusResponse> getBudgetStatus(User user, int month, int year) {

        List<Budget> budgets = budgetRepository
                .findByUserAndBudgetMonthAndBudgetYear(user, month, year);

        return budgets.stream().map(budget -> {

            BigDecimal spent = expenseRepository
                    .findByUserAndCategoryId(user, budget.getCategory().getId())
                    .stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remaining = budget.getAmount().subtract(spent);

            boolean exceeded = spent.compareTo(budget.getAmount()) > 0;

            return BudgetStatusResponse.builder()
                    .categoryName(budget.getCategory().getName())
                    .budgetAmount(budget.getAmount())
                    .spentAmount(spent)
                    .remainingAmount(remaining)
                    .exceeded(exceeded)
                    .build();

        }).toList();
    }
}