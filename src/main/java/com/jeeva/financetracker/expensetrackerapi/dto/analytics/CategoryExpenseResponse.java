package com.jeeva.financetracker.expensetrackerapi.dto.analytics;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CategoryExpenseResponse {

    private String categoryName;
    private BigDecimal totalAmount;

}