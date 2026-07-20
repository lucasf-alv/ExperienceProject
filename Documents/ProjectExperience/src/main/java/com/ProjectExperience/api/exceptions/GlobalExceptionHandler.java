package com.ProjectExperience.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ApiError> UsernotFound(
            UserNotFound ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                java.time.LocalDateTime.now(),
                404,
                "User not found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PhotoError.class)
    public ResponseEntity<ApiError> PhotoError(
            PhotoError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                java.time.LocalDateTime.now(),
                400,
                "A imagem deve ser um arquivo PNG ou JPG",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
