package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.Category;
import com.jeeva.financetracker.expensetrackerapi.entity.CategoryType;
import com.jeeva.financetracker.expensetrackerapi.entity.Income;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.repository.CategoryRepository;
import com.jeeva.financetracker.expensetrackerapi.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeeva.financetracker.expensetrackerapi.exception.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;

    public IncomeServiceImpl(
            IncomeRepository incomeRepository,
            CategoryRepository categoryRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public IncomeResponse createIncome(IncomeRequest request, User user) {


        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("C     ategory not found"));      // it'll throw when the user give the id which is not present

        // 🔐 Ownership check
        if (category.getUser() != null &&
                !category.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot use another user's category");
        }


        // 🚨 Important rule
        if (category.getType() != CategoryType.INCOME) {
            throw new BadRequestException("Category must be INCOME type");    // it will throw if the mentioned category type is not income
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Income amount must be greater than zero");
        }

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Income date cannot be in the future");
        }


        Income income = Income.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();

        return mapToResponse(incomeRepository.save(income));
    }


    @Override
    public List<IncomeResponse> getIncomes(
            User user,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Income> incomes;

        if (categoryId != null && startDate != null && endDate != null) {
            incomes = incomeRepository
                    .findByUserAndCategoryIdAndDateBetween(
                            user, categoryId, startDate, endDate
                    );
        } else if (categoryId != null) {
            incomes = incomeRepository
                    .findByUserAndCategoryId(user, categoryId);
        } else if (startDate != null && endDate != null) {
            incomes = incomeRepository
                    .findByUserAndDateBetween(user, startDate, endDate);
        } else {
            incomes = incomeRepository.findByUser(user);
        }

        return incomes.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public IncomeResponse updateIncome(
            Long incomeId,
            IncomeRequest request,
            User user
    ) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));    // income with id = 99 does not exist in DB

        if (!income.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");              // Income belongs to User A , Logged in User B --> The resource exists, but you are NOT allowed to access or modify it
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getType() != CategoryType.INCOME) {
            throw new BadRequestException("Category must be INCOME type");
        }

        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setDate(request.getDate());
        income.setCategory(category);

        return mapToResponse(incomeRepository.save(income));
    }


    @Override
    public void deleteIncome(Long incomeId, User user) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        incomeRepository.delete(income);
    }

    private IncomeResponse mapToResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .description(income.getDescription())
                .date(income.getDate())
                .categoryName(income.getCategory().getName())
                .build();
    }
}
