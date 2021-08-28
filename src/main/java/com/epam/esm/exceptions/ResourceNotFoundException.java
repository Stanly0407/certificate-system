package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE_KEY = "message.exception.notfound";
    private Locale locale;
    private String resource;

    public ResourceNotFoundException(String resource) {
        this.resource = resource;
    }

    public ResourceNotFoundException() {
    }

    public String getLocalizedMessage() {
        return ErrorResponse.getMessageForLocale(MESSAGE_KEY, locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public static HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    public String getResource() {
        return resource;
    }

}
