package com.ioriocars.ioriocars.config;

import com.ioriocars.ioriocars.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
            .csrf(csrf -> csrf.disable()) // CSRF disabilitato in stile lambda
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**", "/images/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/auto/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auto/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/auto/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/auto/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {
            }); // Basic Auth
        return http.build();
    }
}
