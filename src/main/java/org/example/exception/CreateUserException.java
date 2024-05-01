package org.example.exception;

public class CreateUserException extends RuntimeException{
    public CreateUserException(String message) {
        super(message);
    }
}
