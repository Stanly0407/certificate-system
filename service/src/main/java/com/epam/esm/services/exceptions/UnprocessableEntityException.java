package com.epam.esm.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends Exception {

    private static final String NOT_FOUND_ERROR_KEY = "message.exception.unprocessableEntity.";
    private ExceptionMessageType type;
    private ErrorResponse errorResponse;
    private String resource;

    public UnprocessableEntityException(String resource) {
        this.resource = resource;
    }

    public ErrorResponse getErrorResponse(Locale locale) {
        String messageKey = NOT_FOUND_ERROR_KEY + this.type.getMessageKey();
        String errorMessage = this.resource + ErrorResponse.getMessageForLocale(messageKey, locale);
        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }

}
