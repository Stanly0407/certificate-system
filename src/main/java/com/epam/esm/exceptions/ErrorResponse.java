package com.epam.esm.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorResponse {

    private static final String BUNDLE_BASE_NAME = "messages";
    private String errorMessage;
    private int errorCode;

    public ErrorResponse(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static String getMessageForLocale(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale).getString(messageKey);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
