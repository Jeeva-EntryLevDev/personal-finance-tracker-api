package com.jeeva.financetracker.expensetrackerapi.auth;

import com.jeeva.financetracker.expensetrackerapi.dto.AuthResponse;
import com.jeeva.financetracker.expensetrackerapi.dto.LoginRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.RegisterRequest;
import com.jeeva.financetracker.expensetrackerapi.entity.Role;
import com.jeeva.financetracker.expensetrackerapi.entity.User;
import com.jeeva.financetracker.expensetrackerapi.security.jwt.JwtService;
import com.jeeva.financetracker.expensetrackerapi.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Handle registration
    public void register(RegisterRequest request) {
        // you can add more validation later
        userService.createUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                Role.ROLE_USER
        );
    }

    // Handle login and return JWT
    public AuthResponse login(LoginRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // check password
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        // generate JWT token
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getId(),
                user.getRole().name()
        );
    }
}
