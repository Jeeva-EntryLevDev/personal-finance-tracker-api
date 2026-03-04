package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.Budget;
import com.jeeva.financetracker.expensetrackerapi.entity.Category;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserAndCategoryAndBudgetMonthAndBudgetYear(
            User user,
            Category category,
            int budgetMonth,
            int budgetYear
    );

    List<Budget> findByUserAndBudgetMonthAndBudgetYear(
            User user,
            int budgetMonth,
            int budgetYear
    );
}