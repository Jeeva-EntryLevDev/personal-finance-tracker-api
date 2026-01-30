package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.income.IncomeResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    IncomeResponse createIncome(IncomeRequest request, User user);

    List<IncomeResponse> getIncomes(
            User user,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate
    );


    IncomeResponse updateIncome(
            Long incomeId,
            IncomeRequest request,
            User user
    );



    void deleteIncome(Long incomeId, User user);
}
