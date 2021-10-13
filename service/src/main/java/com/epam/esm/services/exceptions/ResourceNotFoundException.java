package com.epam.esm.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUND_COMMON;
import static com.epam.esm.services.exceptions.ExceptionMessageType.NOT_FOUND_PAGE;

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
    private Integer pageNumber;

    public ResourceNotFoundException(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException() {
        this.resourceId = null;
    }

    public ResourceNotFoundException(Integer pageNumber) {
        this.resourceId = null;
        this.pageNumber = pageNumber;
    }

    public ErrorResponse getErrorResponse(Locale locale) {
        String messageKey = NOT_FOUND_ERROR_KEY + this.type.getMessageKey();
        String errorMessage;
        if (this.resourceId != null) {
            errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale) + this.resourceId;
        } else if (pageNumber != null) {
            messageKey = NOT_FOUND_ERROR_KEY + NOT_FOUND_PAGE.getMessageKey();
            errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale) + ", page - " + this.pageNumber;
        } else {
            messageKey = NOT_FOUND_ERROR_KEY + NOT_FOUND_COMMON.getMessageKey();
            errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale);
        }

        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }

}
