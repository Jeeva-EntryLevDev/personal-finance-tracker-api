package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;

import java.util.List;

public interface CategoryService {

    // Get all categories visible to the current user (global + user-specific)
    List<CategoryResponse> getCategories(User currentUser);

    // Create a new category for the current user
    CategoryResponse createCategory(CategoryRequest request, User currentUser);

    // Update category if owned by the current user (or allow admin later)
    CategoryResponse updateCategory(Long id, CategoryRequest request, User currentUser);

    // Delete category if owned by the current user
    void deleteCategory(Long id, User currentUser);
}
