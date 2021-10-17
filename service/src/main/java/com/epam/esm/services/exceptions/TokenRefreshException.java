package com.epam.esm.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    private static final String FORBIDDEN_ERROR_KEY = "message.exception.tokenRefreshException.";
    private ExceptionMessageType type;

    public TokenRefreshException(ExceptionMessageType type) {
        this.type = type;
    }

    public ErrorResponse getErrorResponse(Locale locale) {
        String messageKey = FORBIDDEN_ERROR_KEY + this.type.getMessageKey();
        String errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale);
        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }

}
