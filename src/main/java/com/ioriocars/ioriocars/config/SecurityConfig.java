package com.ioriocars.ioriocars.config;

import com.ioriocars.ioriocars.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserCache(new NullUserCache());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disabilitato per API REST
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // permette le richieste OPTIONS per tutti (preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // AUTO: chiunque può leggere
                        .requestMatchers(HttpMethod.GET, "/api/auto/**").permitAll()
                        // AUTO: modifiche solo autenticati
                        .requestMatchers("/api/auto/**").authenticated()
                        // USERS: solo admin
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        // Tutto il resto
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // Basic Auth
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",
                "https://ioriocars-fe.onrender.com",
                "https://www.ioriocars.it",
                "https://ioriocars.it"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
