package com.epam.esm.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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

    private String messageKey;

    private int errorCode;

}
