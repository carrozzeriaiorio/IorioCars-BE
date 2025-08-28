package com.ioriocars.ioriocars.config;

import com.ioriocars.ioriocars.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disabilitato per API REST
            .authorizeHttpRequests(auth -> auth
                    // AUTO: chiunque può leggere
                    .requestMatchers(HttpMethod.GET, "/api/auto/**").permitAll()
                    // AUTO: modifiche solo autenticati
                    .requestMatchers("/api/auto/**").authenticated()
                    // USERS: solo admin
                    .requestMatchers("/api/users/**").hasRole("ADMIN")
                    // Tutto il resto
                    .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()); // Basic Auth
        return http.build();
    }
}
