package com.epam.esm.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private static final String NOT_FOUND_ERROR_KEY = "message.exception.notfound.";
    private ExceptionMessageType type;
    private ErrorResponse errorResponse;
    private String resource;

    public ResourceNotFoundException(String resource) {
        this.resource = resource;
    }

    public ErrorResponse getErrorResponse(Locale locale){
        String messageKey = NOT_FOUND_ERROR_KEY + this.type.getMessageKey();
        if(this.resource == null){
            resource = ""; // if no specific resource
        }
        String errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale) + this.resource;
        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }

}
