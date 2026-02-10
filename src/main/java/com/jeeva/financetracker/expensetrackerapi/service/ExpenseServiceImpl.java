package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.expense.ExpenseResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.*;
import com.jeeva.financetracker.expensetrackerapi.exception.*;
import com.jeeva.financetracker.expensetrackerapi.repository.CategoryRepository;
import com.jeeva.financetracker.expensetrackerapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request, User user) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Ownership check
        if (category.getUser() != null &&
                !category.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot use another user's category");
        }

        // Rule: must be EXPENSE
        if (category.getType() != CategoryType.EXPENSE) {
            throw new BadRequestException("Category must be EXPENSE type");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Expense amount must be greater than zero");
        }

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Expense date cannot be in the future");
        }

        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();

        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public List<ExpenseResponse> getExpenses(
            User user,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Expense> expenses;

        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserAndDateBetween(
                    user, startDate, endDate
            );
        } else {
            expenses = expenseRepository.findByUser(user);
        }

        return expenses.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpenses(Long categoryId, User user) {
        List<Expense> expenses;

        if (categoryId != null) {
            expenses = expenseRepository.findByUserAndCategoryId(user, categoryId);
        } else {
            expenses = expenseRepository.findByUser(user);
        }

        return expenses.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExpenseResponse updateExpense(
            Long expenseId,
            ExpenseRequest request,
            User user
    ) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new BadRequestException("Category must be EXPENSE type");
        }

        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());
        expense.setCategory(category);

        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public void deleteExpense(Long expenseId, User user) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .categoryName(expense.getCategory().getName())
                .build();
    }
}
