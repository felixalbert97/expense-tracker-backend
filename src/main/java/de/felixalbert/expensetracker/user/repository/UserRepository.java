package de.felixalbert.expensetracker.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.felixalbert.expensetracker.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
