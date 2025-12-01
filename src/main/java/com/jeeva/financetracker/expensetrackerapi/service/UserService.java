package com.jeeva.financetracker.expensetrackerapi.service;

import com.jeeva.financetracker.expensetrackerapi.entity.Role;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import java.util.Optional;

public interface UserService {

    // Create a new user with encoded password
    User createUser(String fullName, String email, String rawPassword, Role role);

    // Find user by email (used later for login)
    Optional<User> findByEmail(String email);

    // Check if email is already taken
    boolean emailExists(String email);
}