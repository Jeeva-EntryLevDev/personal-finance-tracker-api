package com.jeeva.financetracker.expensetrackerapi.repository;

import com.jeeva.financetracker.expensetrackerapi.entity.Category;
import com.jeeva.financetracker.expensetrackerapi.entity.CategoryType;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Global categories (user null) + categories for the given user
    List<Category> findByUserIsNullOrUser(User user);

    // Useful helper: get categories by type (global + user-specific)
    List<Category> findByTypeAndUserIsNullOrUser(CategoryType type, User user);

    // Prevent duplicate user categories (optional)
    boolean existsByNameAndUser(String name, User user);
}
