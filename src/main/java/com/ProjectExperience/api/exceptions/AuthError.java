package com.ProjectExperience.api.exceptions;

public class AuthError extends RuntimeException {
    public AuthError(String message) {
        super(message);
    }
}
