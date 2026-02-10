package com.jeeva.financetracker.expensetrackerapi.dto.expense;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ExpenseResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private String categoryName;
}
