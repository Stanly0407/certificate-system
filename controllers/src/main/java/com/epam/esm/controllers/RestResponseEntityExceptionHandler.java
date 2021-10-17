package com.epam.esm.controllers;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.exceptions.TokenRefreshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.Locale;

import static com.epam.esm.services.exceptions.ExceptionMessageType.INTERNAL_ERROR;
import static com.epam.esm.services.exceptions.ExceptionMessageType.INVALID_INPUT;
import static com.epam.esm.services.exceptions.ExceptionMessageType.INVALID_PATH_VARIABLE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.METHOD_NOT_SUPPORTED;
import static com.epam.esm.services.exceptions.ExceptionMessageType.MISSING_PATH_VARIABLE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUND_PAGE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUND_RESOURCE;
import static com.epam.esm.services.exceptions.ExceptionMessageType.UNKNOWN_PROPERTIES;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException exception, WebRequest request) {
        Locale locale = request.getLocale();
        ErrorResponse error = exception.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler({BadRequestException.class})
    protected final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        Locale locale = request.getLocale();
        ErrorResponse error = exception.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(METHOD_NOT_SUPPORTED).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        exception.setType(NOT_FOUND_RESOURCE);
        ErrorResponse error = exception.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        ResourceNotFoundException resourceNotFoundException = ResourceNotFoundException.builder().type(NOT_FOUND_PAGE).build();
        ErrorResponse error = resourceNotFoundException.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_INPUT).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.info("MethodArgumentNotValidException: " + exception.getMessage());
        parseMethodArgumentNotValidExceptionMessage(error, exception);
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(MISSING_PATH_VARIABLE).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.info("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleNotValidPathVariable(MethodArgumentTypeMismatchException exception, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_PATH_VARIABLE).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        LOGGER.info("MethodArgumentTypeMismatchException - " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<?> exceptionHandler(ValidationException exception, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(INVALID_INPUT).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        if (exception instanceof javax.validation.ConstraintViolationException) {
            parseConstraintViolationException(error, (javax.validation.ConstraintViolationException) exception);
        }
        LOGGER.info("Exception message: " + exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        BadRequestException badRequestException = BadRequestException.builder().type(UNKNOWN_PROPERTIES).build();
        ErrorResponse error = badRequestException.getErrorResponse(locale);
        parseHttpMessageNotReadableExceptionMessage(error, exception);
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
        LOGGER.info("Exception: " + exception + "\n Exception message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void parseHttpMessageNotReadableExceptionMessage(ErrorResponse error, HttpMessageNotReadableException exception) {
        String exceptionMessage = exception.getLocalizedMessage();
        String[] params = exceptionMessage.split("\"");
        String messageParam = "";
        if (params.length > 1) {
            messageParam = " - " + params[1];
        }
        error.setErrorMessage(error.getErrorMessage() + messageParam);
    }

    private void parseConstraintViolationException(ErrorResponse error, javax.validation.ConstraintViolationException exception) {
        String exceptionMessage = exception.getConstraintViolations().toString();
        String message = exceptionMessage.split("\\'")[1];
        String messageParam = exceptionMessage.split("propertyPath=")[1].split("\\,")[0].split("\\.")[1];
        error.setErrorMessage(error.getErrorMessage() + ", " + messageParam + " - " + message);
    }

    private void parseMethodArgumentNotValidExceptionMessage(ErrorResponse error, MethodArgumentNotValidException exception) {
        String[] messages = exception.getLocalizedMessage().split("default message \\[");
        String messageParam = messages[1].split("\\]")[0];
        String message = messages[2].split("\\]")[0];
        error.setErrorMessage(error.getErrorMessage() + ", " + messageParam + " - " + message);
    }

}
