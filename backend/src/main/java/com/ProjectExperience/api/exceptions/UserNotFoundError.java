package com.ProjectExperience.api.exceptions;

public class UserNotFoundError extends RuntimeException{
   public UserNotFoundError(String message){
        super(message);
    }
}
