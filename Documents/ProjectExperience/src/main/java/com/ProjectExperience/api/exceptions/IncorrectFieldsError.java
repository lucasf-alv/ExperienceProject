package com.ProjectExperience.api.exceptions;

public class IncorrectFieldsError extends RuntimeException {
    public IncorrectFieldsError(String message) {
        super(message);
    }
}
