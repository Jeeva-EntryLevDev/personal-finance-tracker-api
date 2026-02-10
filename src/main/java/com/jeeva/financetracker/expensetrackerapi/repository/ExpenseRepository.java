package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.Expense;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndDateBetween(
            User user,
            LocalDate start,
            LocalDate end
    );

    List<Expense> findByUserAndCategoryId(User user, Long categoryId);
}
