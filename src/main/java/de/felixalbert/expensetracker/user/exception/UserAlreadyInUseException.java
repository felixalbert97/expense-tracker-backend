package de.felixalbert.expensetracker.user.exception;

public class UserAlreadyInUseException extends RuntimeException{

    public UserAlreadyInUseException() {
        super("Email is already in use");
    }
}
