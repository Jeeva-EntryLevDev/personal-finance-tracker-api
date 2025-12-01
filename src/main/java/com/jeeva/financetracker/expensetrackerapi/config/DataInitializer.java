package com.jeeva.financetracker.expensetrackerapi.config;

import com.jeeva.financetracker.expensetrackerapi.entity.Role;
import com.jeeva.financetracker.expensetrackerapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserService userService) {
        return args -> {

            String email = "admin@demo.com";

            // If admin doesn't already exist, create one
            if (!userService.emailExists(email)) {
                userService.createUser(
                        "Jeeva Admin",
                        email,
                        "admin123",      // raw password (will be encoded)
                        Role.ROLE_ADMIN
                );

                System.out.println("🟢 Admin user created: " + email);
            } else {
                System.out.println("ℹ️ Admin user already exists. Skipping creation.");
            }
        };
    }
}
