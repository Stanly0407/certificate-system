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
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

    private static final String BAD_REQUEST_ERROR_KEY = "message.exception.badRequest.";
    private ExceptionMessageType type;
    private ErrorResponse errorResponse;

    public BadRequestException(ExceptionMessageType type) {
        this.type = type;
    }

    public ErrorResponse getErrorResponse(Locale locale){
        String messageKey = BAD_REQUEST_ERROR_KEY + this.type.getMessageKey();
        String errorMessage = ErrorResponse.getMessageForLocale(messageKey, locale);
        int errorCode = this.type.getErrorCode();
        return ErrorResponse.builder().errorMessage(errorMessage).errorCode(errorCode).build();
    }


}
