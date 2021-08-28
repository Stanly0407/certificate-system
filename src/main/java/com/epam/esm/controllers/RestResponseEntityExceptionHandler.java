package com.epam.esm.controllers;

import com.epam.esm.exceptions.ErrorResponse;
import com.epam.esm.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
    private static final String INTERNAL_ERROR = "message.exception.internal";
    private static final String PAGE_NOT_FOUND_ERROR = "message.exception.page.notfound";

    @ExceptionHandler(ResourceNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        exception.setLocale(locale);
        String errorMessage = exception.getLocalizedMessage() + exception.getResource();
        LOGGER.debug("errorMessage = " + errorMessage);
        ErrorResponse error = new ErrorResponse(errorMessage, ResourceNotFoundException.getHttpStatus());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(PAGE_NOT_FOUND_ERROR, locale);
        ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(INTERNAL_ERROR, locale);
        ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}

