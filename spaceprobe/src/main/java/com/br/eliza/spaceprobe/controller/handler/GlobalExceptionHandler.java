package com.br.eliza.spaceprobe.controller.handler;

import com.br.eliza.spaceprobe.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoverNotFoundException.class)
    public ResponseEntity<String> handleRoverNotFoundException(RoverNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PlanetNotFoundException.class)
    public ResponseEntity<String> handlePlanetNotFoundException(PlanetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CoordinateOccupiedException.class)
    public ResponseEntity<String> handleCoordinateOccupiedException(CoordinateOccupiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PlanetFullException.class)
    public ResponseEntity<String> handlePlanetFullException(PlanetFullException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<String> handleInvalidCommandException(InvalidCommandException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> handleRuntimeException() {
        var error = StandardError.builder()
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> argumentoNotValid(
            MethodArgumentNotValidException ex) {
        ValidationError vd = ValidationError.builder()
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .build();

        for(FieldError x : ex.getBindingResult().getFieldErrors()) {
            vd.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST).body(vd);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        var error = StandardError.builder()
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    //json parse error
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {
        var error = StandardError.builder()
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
}
