package de.felixalbert.expensetracker.user.model;

public record LoginRequest(
    String email,
    String password
) {}
