package com.ProjectExperience.api.exceptions;

public class IncorrectConfirmationCodeError extends RuntimeException {
    public IncorrectConfirmationCodeError(String message) {
        super(message);
    }
}
