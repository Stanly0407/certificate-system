package com.epam.esm.controllers;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
    private static final String INTERNAL_ERROR = "message.exception.internal";
    private static final String BAD_REQUEST_ERROR = "message.exception.badRequest";
    private static final String PAGE_NOT_FOUND_ERROR = "message.exception.page.notfound";

    @ExceptionHandler({BadRequestException.class})
    protected final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        Locale locale = request.getLocale();
        String messageKey = "message.exception.badRequest."+ exception.getMessageKey();
        String errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale);
        int errorCode = exception.getErrorCode();
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
// codes:
// 40001 - common.
// 40002 - RequestMethodNotSupported.
// 40003 - The request contain incorrect parameters
// 40004- invalid input
// 40005 - exists
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        exception.setLocale(locale);
        String errorMessage = exception.getLocalizedMessage() + exception.getResource();
        int errorCode = ResourceNotFoundException.getErrorCode();
        LOGGER.debug("errorMessage = " + errorMessage);
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return new ResponseEntity<>(error, ResourceNotFoundException.getHttpStatus());
    }



    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(
                "message.exception.badRequest.RequestMethodNotSupported", locale);
        int errorCode = 40002; // Http request method not supported
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(PAGE_NOT_FOUND_ERROR, locale);
        int errorCode = 40402;
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(BAD_REQUEST_ERROR, locale);

        int errorCode = 1111;
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage + exception.getMessage())
                .errorCode(errorCode).build();
        return  ResponseEntity.status( HttpStatus.BAD_REQUEST).body(error);
      //  return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // handle @Validated
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<?> exceptionHandler(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage() + " ++++++++++");
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(BAD_REQUEST_ERROR, locale);
        //  + "Exception: " + exception.getMessage() // todo new logging

        int errorCode = 2222;
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleNotValidPathVariable(Exception exception, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(BAD_REQUEST_ERROR, locale);
        //  + "Exception: " + exception.getMessage() // todo new logging

        int errorCode = 40005;
        ErrorResponse error = ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
        return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error + " ++++++++++");
       // return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<?> exceptionHandler(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage() + "  3333333333");
    }



    @Override
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(INTERNAL_ERROR, locale);
        int errorCode = 50001;
        ErrorResponse error = ErrorResponse.builder()
                .errorMessage((errorMessage + " + " + exception.getMessage() + " + " + exception))
                .errorCode(errorCode).build();
        LOGGER.error("Internal Error: " + exception + " | Message: " + exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
