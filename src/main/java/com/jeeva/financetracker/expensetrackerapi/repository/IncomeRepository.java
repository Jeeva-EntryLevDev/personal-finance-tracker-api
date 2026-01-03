package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.Income;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    // Get all incomes of a user
    List<Income> findByUser(User user);

    // Filter by date range
    List<Income> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
