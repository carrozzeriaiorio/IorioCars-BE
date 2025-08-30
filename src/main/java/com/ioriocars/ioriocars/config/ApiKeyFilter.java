package com.ioriocars.ioriocars.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY = "iorio123";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Escludi endpoint pubblici
        if (path.startsWith("/api/auth") || path.equals("/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verifica chiave
        String key = request.getHeader("X-API-KEY");
        if (!API_KEY.equals(key)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Chiave API mancante o errata");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
