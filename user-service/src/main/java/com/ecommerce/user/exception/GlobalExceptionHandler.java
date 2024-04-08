package com.ecommerce.user.exception;

import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.ExceptionHandler;import org.springframework.web.bind.annotation.RestControllerAdvice;import java.util.HashMap;import java.util.Map;import java.util.Objects;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleExists(ResourceAlreadyExistsException resourceAlreadyExistsException) {
        Map<String, Object> mp = Map.of("Error", "Conflict", "Message",resourceAlreadyExistsException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mp);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException resourceNotFoundException) {
        Map<String, Object> mp = Map.of("Error","Not Found", "Message", resourceNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String , Object> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> error.put(err.getField(), err.getDefaultMessage()));
        Map<String, Object> mp = Map.of("Error", "Validation Failed", "Details",error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        Map<String, Object> mp = Map.of("Error", "Internal Server Error", "Message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mp);
    }
}

