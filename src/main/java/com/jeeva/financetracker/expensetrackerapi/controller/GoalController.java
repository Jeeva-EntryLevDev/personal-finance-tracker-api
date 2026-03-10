package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.goal.*;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.GoalService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public GoalResponse createGoal(
            @RequestBody GoalRequest request,
            @AuthenticationPrincipal User user
    ) {
        return goalService.createGoal(request, user);
    }

    @GetMapping
    public List<GoalResponse> getGoals(@AuthenticationPrincipal User user) {
        return goalService.getGoals(user);
    }

    @PutMapping("/{id}")
    public GoalResponse updateGoal(
            @PathVariable Long id,
            @RequestBody GoalRequest request,
            @AuthenticationPrincipal User user
    ) {
        return goalService.updateGoal(id, request, user);
    }

    @PostMapping("/{id}/contribute")
    public GoalResponse contribute(
            @PathVariable Long id,
            @RequestBody GoalContributionRequest request,
            @AuthenticationPrincipal User user
    ) {
        return goalService.contributeToGoal(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        goalService.deleteGoal(id, user);
    }
}