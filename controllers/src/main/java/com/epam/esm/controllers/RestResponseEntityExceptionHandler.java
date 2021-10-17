package com.epam.esm.controllers;

import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
    private static final String INTERNAL_ERROR = "message.exception.internal";
    private static final String BAD_REQUEST_ERROR = "message.exception.badRequest";
    private static final String PAGE_NOT_FOUND_ERROR = "message.exception.page.notfound";

    @ExceptionHandler({ResourceNotFoundException.class})
    protected final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        exception.setLocale(locale);
        String errorMessage = exception.getLocalizedMessage() + exception.getResource();
        int errorCode = ResourceNotFoundException.getErrorCode();
        LOGGER.debug("errorMessage = " + errorMessage);
        ErrorResponse error = new ErrorResponse(errorMessage, errorCode);
        return new ResponseEntity<>(error, ResourceNotFoundException.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(PAGE_NOT_FOUND_ERROR, locale);
        int errorCode = 40402;
        ErrorResponse error = new ErrorResponse(errorMessage, errorCode);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(BAD_REQUEST_ERROR, locale);
        int errorCode = 40001;
        ErrorResponse error = new ErrorResponse(errorMessage, errorCode);
        LOGGER.error("Error: " + exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleNotValidPathVariable(Exception exception, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(BAD_REQUEST_ERROR, locale);
        int errorCode = 40002;
        ErrorResponse error = new ErrorResponse(errorMessage, errorCode);
        LOGGER.error("Error: " + exception + " | Message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(INTERNAL_ERROR, locale);
        int errorCode = 50001;
        ErrorResponse error = new ErrorResponse(errorMessage, errorCode);
        LOGGER.error("Internal Error: " + exception + " | Message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
