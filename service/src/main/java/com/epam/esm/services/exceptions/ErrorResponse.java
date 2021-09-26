package com.epam.esm.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;
import java.util.ResourceBundle;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private static final String BUNDLE_BASE_NAME = "messages";
    private String errorMessage;
    private int errorCode;

    public static String getMessageForLocale(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale).getString(messageKey);
    }

}
