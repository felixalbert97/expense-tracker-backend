package de.felixalbert.expensetracker.security.model;

public record LoginRequest(
    String email,
    String password
) {}
