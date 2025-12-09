package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.category.CategoryResponse;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /categories  -> global + user-specific
    @GetMapping
    public List<CategoryResponse> getCategories(@AuthenticationPrincipal User user) {
        return categoryService.getCategories(user);
    }

    // POST /categories
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestBody CategoryRequest request,
                                   @AuthenticationPrincipal User user) {
        return categoryService.createCategory(request, user);
    }

    // PUT /categories/{id}
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @RequestBody CategoryRequest request,
                                   @AuthenticationPrincipal User user) {
        return categoryService.updateCategory(id, request, user);
    }

    // DELETE /categories/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(id, user);
    }
}
