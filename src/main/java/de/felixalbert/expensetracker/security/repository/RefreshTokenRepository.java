package de.felixalbert.expensetracker.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.felixalbert.expensetracker.security.model.RefreshToken;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}
