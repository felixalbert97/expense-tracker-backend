package de.felixalbert.expensetracker.user.service;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
        "super-secret-key-change-me-but-i-must-be-veeeeery-loooong";

    private static final long EXPIRATION_MS =
        1000 * 60 * 60; // 1h

    public String generateToken(Authentication authentication) {
        return Jwts.builder()
            .setSubject(authentication.getName()) // email
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + EXPIRATION_MS)
            )
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .compact();
    }
}
