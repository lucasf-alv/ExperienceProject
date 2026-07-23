package com.ProjectExperience.api.exceptions;

public class ActivityCompletedError extends RuntimeException {
    public ActivityCompletedError(String message) {
        super(message);
    }
}
