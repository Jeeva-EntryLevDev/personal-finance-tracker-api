package com.jeeva.financetracker.expensetrackerapi.dto.goal;

import com.jeeva.financetracker.expensetrackerapi.entity.GoalStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class GoalResponse {

    private Long id;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private LocalDate targetDate;
    private GoalStatus status;
}