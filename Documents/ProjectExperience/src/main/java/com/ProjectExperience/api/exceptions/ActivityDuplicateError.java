package com.ProjectExperience.api.exceptions;

public class ActivityDuplicateError extends RuntimeException {
    public ActivityDuplicateError(String message) {
        super(message);
    }
}
