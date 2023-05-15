package com.tcc.app.web.memory_game.api.exceptions;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.NoPermissionException;
import java.awt.color.ProfileDataException;

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
    
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity usernameNotFound(UsernameNotFoundException ignored) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário não encontrado!");
//    }
   
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity badCredentials(BadCredentialsException ignored) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário/Senha errada!");
    }
    
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity invalidValue(InvalidValueException invalidValueException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(invalidValueException.getMessage());
    }
    
    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity noPermission(NoPermissionException noPermissionException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(noPermissionException.getMessage());
    }
    
    @ExceptionHandler(ProfileDataException.class)
    public ResponseEntity profileDataException(ProfileDataException profileDataException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(profileDataException.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity unusualException(Exception ignored) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Deu algo errado! Favor contatar o administrador!");
    }
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity exceptionCommon(Exception exception) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
//    }
}