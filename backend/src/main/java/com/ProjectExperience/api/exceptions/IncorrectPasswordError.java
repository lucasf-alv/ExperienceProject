package com.ProjectExperience.api.exceptions;

public class IncorrectPasswordError extends RuntimeException {
    public IncorrectPasswordError(String message) {
        super(message);
    }
}
