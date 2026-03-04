package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.budget.BudgetResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.*;
import com.jeeva.financetracker.expensetrackerapi.exception.*;
import com.jeeva.financetracker.expensetrackerapi.repository.BudgetRepository;
import com.jeeva.financetracker.expensetrackerapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public BudgetServiceImpl(
            BudgetRepository budgetRepository,
            CategoryRepository categoryRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BudgetResponse createBudget(BudgetRequest request, User user) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new BadRequestException("Budget can be created only for EXPENSE category");
        }

        budgetRepository.findByUserAndCategoryAndBudgetMonthAndBudgetYear(
                user, category, request.getMonth(), request.getYear()
        ).ifPresent(b -> {
            throw new BadRequestException("Budget already exists for this category and month");
        });

        Budget budget = Budget.builder()
                .amount(request.getAmount())
                .budgetMonth(request.getMonth())
                .budgetYear(request.getYear())
                .category(category)
                .user(user)
                .build();

        return mapToResponse(budgetRepository.save(budget));
    }

    @Override
    public List<BudgetResponse> getBudgets(User user, int month, int year) {

        return budgetRepository.findByUserAndBudgetMonthAndBudgetYear(user, month, year)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BudgetResponse updateBudget(
            Long budgetId,
            BudgetRequest request,
            User user
    ) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        budget.setAmount(request.getAmount());

        return mapToResponse(budgetRepository.save(budget));
    }

    @Override
    public void deleteBudget(Long budgetId, User user) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        budgetRepository.delete(budget);
    }

    private BudgetResponse mapToResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .month(budget.getBudgetMonth())
                .year(budget.getBudgetYear())
                .categoryName(budget.getCategory().getName())
                .build();
    }
}