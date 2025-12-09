package com.jeeva.financetracker.expensetrackerapi.dto.category;

import com.jeeva.financetracker.expensetrackerapi.entity.CategoryType;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private CategoryType type;
}
