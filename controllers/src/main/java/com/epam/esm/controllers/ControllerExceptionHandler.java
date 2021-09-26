package com.epam.esm.controllers;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Locale;

@ControllerAdvice
public class ControllerExceptionHandler {
//    @ResponseBody
//
//    @ExceptionHandler(ValidationException.class)
//    ResponseEntity<?>  exceptionHandler(ValidationException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + "  3333333333");
//    }
//
//    @ResponseBody
//    @ExceptionHandler(ConstraintViolationException.class)
//    ResponseEntity<?> exceptionHandler(ConstraintViolationException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + " ++++++++++");
//    }



}
