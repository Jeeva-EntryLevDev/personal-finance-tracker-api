package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.Income;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    List<Income> findByUserAndDateBetween(
            User user,
            LocalDate start,
            LocalDate end
    );

    List<Income> findByUserAndCategoryId(
            User user,
            Long categoryId
    );

    List<Income> findByUserAndCategoryIdAndDateBetween(
            User user,
            Long categoryId,
            LocalDate start,
            LocalDate end
    );
}
