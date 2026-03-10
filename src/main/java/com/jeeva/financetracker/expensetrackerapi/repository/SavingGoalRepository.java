package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.SavingGoal;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingGoalRepository extends JpaRepository<SavingGoal, Long> {

    List<SavingGoal> findByUser(User user);

}