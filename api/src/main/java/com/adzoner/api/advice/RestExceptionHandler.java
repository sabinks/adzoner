package com.adzoner.api.advice;

import com.adzoner.api.response.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleException(StudentNotFoundException exception){
//
//        ErrorResponse errorResponse =  new ErrorResponse();
//
//        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
//        errorResponse.setMessage(exception.getMessage());
//        errorResponse.setTimeStamp(LocalDateTime.now().toString());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }
//    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodNotAllowedException exception){
        ErrorResponse errorResponse =  new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimeStamp(LocalDateTime.now().toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception(Exception exception){
        ErrorResponse errorResponse =  new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimeStamp(LocalDateTime.now().toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception (EmptyResultDataAccessException exception){
        ErrorResponse errorResponse =  new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimeStamp(LocalDateTime.now().toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
}
