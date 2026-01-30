package de.felixalbert.expensetracker.security.component;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import de.felixalbert.expensetracker.common.exception.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException ex
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorType = (String) request.getAttribute("auth_error");

        ApiError error;

        if ("TOKEN_EXPIRED".equals(errorType)) {
            error = new ApiError(
                Instant.now(),
                401,
                "TOKEN_EXPIRED",
                "Session expired. Please log in again.",
                request.getRequestURI(),
                null
            );
        } else {
            error = new ApiError(
                Instant.now(),
                401,
                "UNAUTHORIZED",
                "Authentication required",
                request.getRequestURI(),
                null
            );
        }

        response.getWriter().write(
            new ObjectMapper().writeValueAsString(error)
        );
        response.getWriter().flush();
    }
} 