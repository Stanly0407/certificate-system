package com.epam.esm.controllers;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Locale;

import static com.epam.esm.services.exceptions.ExceptionMessageType.INCORRECT_PARAMETERS;
import static com.epam.esm.services.exceptions.ExceptionMessageType.INTERNAL_ERROR;
import static com.epam.esm.services.exceptions.ExceptionMessageType.INVALID_INPUT;
import static com.epam.esm.services.exceptions.ExceptionMessageType.INVALID_PATH_VARIABLE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.METHOD_NOT_SUPPORTED;
import static com.epam.esm.services.exceptions.ExceptionMessageType.MISSING_PATH_VARIABLE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUNT_PAGE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUNT_RESOURCE;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler({BadRequestException.class})
    protected final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        Locale locale = request.getLocale();
        ErrorResponse error = exception.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(METHOD_NOT_SUPPORTED).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        exception.setType(NOT_FOUNT_RESOURCE);
        ErrorResponse error = exception.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        ResourceNotFoundException resourceNotFoundException = ResourceNotFoundException.builder().type(NOT_FOUNT_PAGE).build();
        ErrorResponse error = resourceNotFoundException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_INPUT).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> exceptionHandler(ConstraintViolationException exception, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INCORRECT_PARAMETERS).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(MISSING_PATH_VARIABLE).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleNotValidPathVariable(WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_PATH_VARIABLE).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<?> exceptionHandler(ValidationException exception, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_INPUT).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_INPUT).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(INTERNAL_ERROR.getMessageKey(), locale);
        int errorCode = INTERNAL_ERROR.getErrorCode();
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        LOGGER.error("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
