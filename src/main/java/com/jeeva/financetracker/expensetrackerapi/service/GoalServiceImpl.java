package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.goal.*;
import com.jeeva.financetracker.expensetrackerapi.entity.*;
import com.jeeva.financetracker.expensetrackerapi.exception.*;
import com.jeeva.financetracker.expensetrackerapi.repository.SavingGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class GoalServiceImpl implements GoalService {

    private final SavingGoalRepository goalRepository;

    public GoalServiceImpl(SavingGoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public GoalResponse createGoal(GoalRequest request, User user) {

        SavingGoal goal = SavingGoal.builder()
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .savedAmount(BigDecimal.ZERO)
                .targetDate(request.getTargetDate())
                .status(GoalStatus.IN_PROGRESS)
                .user(user)
                .build();

        return map(goalRepository.save(goal));
    }

    @Override
    public List<GoalResponse> getGoals(User user) {

        return goalRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public GoalResponse updateGoal(Long goalId, GoalRequest request, User user) {

        SavingGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setTargetDate(request.getTargetDate());

        return map(goalRepository.save(goal));
    }

    @Override
    public GoalResponse contributeToGoal(Long goalId, GoalContributionRequest request, User user) {

        SavingGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        BigDecimal newAmount = goal.getSavedAmount().add(request.getAmount());
        goal.setSavedAmount(newAmount);

        if (newAmount.compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(GoalStatus.COMPLETED);
        }

        return map(goalRepository.save(goal));
    }

    @Override
    public void deleteGoal(Long goalId, User user) {

        SavingGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        goalRepository.delete(goal);
    }

    private GoalResponse map(SavingGoal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .targetAmount(goal.getTargetAmount())
                .savedAmount(goal.getSavedAmount())
                .targetDate(goal.getTargetDate())
                .status(goal.getStatus())
                .build();
    }
}