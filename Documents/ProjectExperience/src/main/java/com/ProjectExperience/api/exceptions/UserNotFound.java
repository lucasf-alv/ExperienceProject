package com.ProjectExperience.api.exceptions;

public class UserNotFound extends RuntimeException{
   public UserNotFound(String message){
        super(message);
    }
}
