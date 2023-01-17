package com.tcc.app.web.memory_game.api.infrastructures.exceptions;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity notFound(EntityNotFoundException entityNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entityNotFoundException.getMessage());
    }
    
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity exists(EntityExistsException entityExistsException){
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(entityExistsException.getMessage());
    }
    
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity invalidValue(InvalidValueException invalidValueException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(invalidValueException.getMessage());
    }
}