package com.jeeva.financetracker.expensetrackerapi.dto.budget;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BudgetResponse {

    private Long id;
    private BigDecimal amount;
    private int month;
    private int year;
    private String categoryName;
}