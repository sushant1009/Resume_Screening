package com.example.Resume_Screening_Backend.Exceptions;

import com.example.Resume_Screening_Backend.dto.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicateKey(DataIntegrityViolationException ex) {

        String errorMessage = ex.getMostSpecificCause().getMessage().toLowerCase();

        if (errorMessage.contains("gmail")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiError("email", "Email already exists"));
        }

        if (errorMessage.contains("username")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiError("username", "Username already exists"));
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError("unknown", "Duplicate value"));
    }
}

