package de.felixalbert.expensetracker.security.model;

public record RegisterRequest(
    String email,
    String password
) {}