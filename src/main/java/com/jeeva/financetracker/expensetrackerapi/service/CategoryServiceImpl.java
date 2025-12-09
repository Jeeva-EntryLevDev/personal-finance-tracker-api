package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.Category;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all categories (global + user-specific)
    @Override
    public List<CategoryResponse> getCategories(User currentUser) {
        return categoryRepository.findByUserIsNullOrUser(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create a new category
    @Override
    public CategoryResponse createCategory(CategoryRequest request, User currentUser) {
        // optional: prevent duplicate for the same user
        if (categoryRepository.existsByNameAndUser(request.getName(), currentUser)) {
            throw new IllegalArgumentException("Category with same name already exists for this user");
        }

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(currentUser)                // user-specific category
                .build();

        return toResponse(categoryRepository.save(category));
    }

    // Update category (only if owned)
    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request, User currentUser) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // only owner can update user-specific category
        if (category.getUser() != null &&
                !category.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Forbidden");
        }

        category.setName(request.getName());
        category.setType(request.getType());

        return toResponse(categoryRepository.save(category));
    }

    // Delete category (only if owned)
    @Override
    public void deleteCategory(Long id, User currentUser) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getUser() != null &&
                !category.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Forbidden");
        }

        categoryRepository.delete(category);
    }

    // Convert entity → DTO
    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setGlobal(category.getUser() == null);
        return response;
    }
}
