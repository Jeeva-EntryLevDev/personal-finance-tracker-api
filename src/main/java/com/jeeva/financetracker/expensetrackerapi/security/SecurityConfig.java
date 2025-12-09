//package com.jeeva.financetracker.expensetrackerapi.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration                       // It tells Spring: “This class defines configuration beans — treat it like a setup file.”
//@EnableWebSecurity                  // This tells Spring Boot:  “I’m providing my own custom security configuration, so please disable your default one.”
//public class SecurityConfig {
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {                // You’re creating a bean of type SecurityFilterChain.This is the pipeline of filters that runs on every request.It decides whether a request should be allowed, blocked, or redirected to login.
//        http
//                .csrf(csrf -> csrf.disable())                   // Disables Cross-Site Request Forgery protection.   Because CSRF protection is only needed for web apps with HTML forms.
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()                                      // “Spring Security, allow every single request — even without login, even without JWT.”
//                )
//                .headers(headers -> headers
//                        .frameOptions(frame -> frame.disable())
//                )
//                .formLogin(form -> form.disable()) // ✅ disable default login form
//                .httpBasic(basic -> basic.disable()); // ✅ disable basic auth popup
//
//        return http.build();
//
//    }
//}


package com.jeeva.financetracker.expensetrackerapi.security;

import com.jeeva.financetracker.expensetrackerapi.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // REST APIs don't use CSRF
                .csrf(csrf -> csrf.disable())

                // No HTTP session — JWT only
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no JWT required)
                        .requestMatchers(
                                "/auth/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Everything else needs JWT
                        .anyRequest().authenticated()
                )

                // Needed for H2 console
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                // Disable browser-based auth
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
