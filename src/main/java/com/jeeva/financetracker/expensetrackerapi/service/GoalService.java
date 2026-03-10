package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.goal.*;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.util.List;

public interface GoalService {

    GoalResponse createGoal(GoalRequest request, User user);

    List<GoalResponse> getGoals(User user);

    GoalResponse updateGoal(Long goalId, GoalRequest request, User user);

    GoalResponse contributeToGoal(Long goalId, GoalContributionRequest request, User user);

    void deleteGoal(Long goalId, User user);
}