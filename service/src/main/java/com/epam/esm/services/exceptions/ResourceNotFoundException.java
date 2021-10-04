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
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private static final String NOT_FOUND_ERROR_KEY = "message.exception.notfound.";
    private ExceptionMessageType type;
    private ErrorResponse errorResponse;
    private Long resourceId;

    public ResourceNotFoundException(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException() {
        this.resourceId = null;
    }

    public ErrorResponse getErrorResponse(Locale locale) {
        String messageKey = NOT_FOUND_ERROR_KEY + this.type.getMessageKey();
        String errorMessage;
        if (this.resourceId != null) {
            errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale) + this.resourceId;
        } else {
            errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale);
        }
        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }

}
