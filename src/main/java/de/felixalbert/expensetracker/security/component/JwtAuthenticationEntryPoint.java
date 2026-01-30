package de.felixalbert.expensetracker.security.component;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String error = (String) request.getAttribute("auth_error");

        if ("TOKEN_EXPIRED".equals(error)) {
            response.getWriter().write("""
                { "message": "Session expired. Please log in again." }
            """);
        } else {
            response.getWriter().write("""
                { "message": "Authentication required." }
            """);
        }

        response.getWriter().flush();
        response.getWriter().close();
    }
}
