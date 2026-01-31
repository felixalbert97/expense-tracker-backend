package de.felixalbert.expensetracker.security.service;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import de.felixalbert.expensetracker.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
        "super-secret-key-change-me-but-i-must-be-veeeeery-loooong";

    private static final long EXPIRATION_MS =
        1000 * 60 * 60; // 1h

    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName());
    }

    public String generateToken(User user) {
        return generateToken(user.getEmail());
    }

    public String generateToken(String userName) {
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + EXPIRATION_MS)
            )
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
            .getExpiration()
            .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
