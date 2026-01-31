package de.felixalbert.expensetracker.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.felixalbert.expensetracker.security.exception.InvalidRefreshTokenException;
import de.felixalbert.expensetracker.security.exception.RefreshTokenExpiredException;
import de.felixalbert.expensetracker.security.model.RefreshToken;
import de.felixalbert.expensetracker.security.repository.RefreshTokenRepository;
import de.felixalbert.expensetracker.user.model.User;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(
            Instant.now().plus(30, ChronoUnit.DAYS)
        );
        refreshToken.setRevoked(false);

        return repository.save(refreshToken);
    }

    public void revokeByToken(String token) {
        repository.findByToken(token) //no error: no security leak!
            .ifPresent(rt -> {
                rt.setRevoked(true);
                repository.save(rt);
            });
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
            .orElseThrow(() -> new InvalidRefreshTokenException());

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException();
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException();
        }

        return refreshToken;
    }

    public RefreshToken rotate(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        repository.save(oldToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(oldToken.getUser());
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        return repository.save(newToken);
    }
}