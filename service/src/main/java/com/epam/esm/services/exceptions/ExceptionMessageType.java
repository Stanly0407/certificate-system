package com.epam.esm.services.exceptions;

public enum ExceptionMessageType {
    // see also README.md

    COMMON_BAD_REQUEST("common", 40001),
    METHOD_NOT_SUPPORTED("RequestMethodNotSupported", 40002),
    INCORRECT_PARAMETERS("params", 40003),
    INVALID_INPUT("invalid.input", 40004),
    INVALID_PATH_VARIABLE("invalid.path.variable", 40005),
    MISSING_PATH_VARIABLE("MissingPathVariable", 40006),
    ALREADY_EXISTS("exists", 40007),

    NOT_FOUNT_RESOURCE("common", 40401),
    NOT_FOUNT_PAGE("page", 40402),

    INTERNAL_ERROR("message.exception.internal", 50001);

    String messageKey;
    int errorCode;

    ExceptionMessageType(String messageKey, int errorCode) {
        this.messageKey = messageKey;
        this.errorCode = errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
