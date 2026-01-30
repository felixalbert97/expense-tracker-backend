package de.felixalbert.expensetracker.user.model;

public record RegisterRequest(
    String email,
    String password
) {}