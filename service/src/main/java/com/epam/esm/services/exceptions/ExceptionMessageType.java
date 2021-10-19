package com.epam.esm.services.exceptions;

public enum ExceptionMessageType {
    // see also README.md

    COMMON_BAD_REQUEST("common", 40001),
    METHOD_NOT_SUPPORTED("RequestMethodNotSupported", 40501),
    INCORRECT_PARAMETERS("params", 40003),
    INVALID_INPUT("invalid.input", 40004),
    INVALID_PATH_VARIABLE("invalid.path.variable", 40005),
    MISSING_PATH_VARIABLE("MissingPathVariable", 40006),
    ALREADY_EXISTS("exists", 40007),
    NOT_BLANK("notBlank", 40008),
    UNKNOWN_PROPERTIES("unknownProperties", 40009),
    INCORRECT_SORT("incorrectSort", 40010),
    MISSING_HEADER("MissingRequestHeaderException", 40011),

    UNAUTHORIZED("message.security.unauthorized", 40101),
    EXPIRED("message.exception.tokenRefreshException.expired", 40102),
    INCORRECT_TOKEN("message.exception.tokenRefreshException.unknown", 40103),
    UNKNOWN_USER("message.security.unknownUser", 40104),

    FORBIDDEN("message.security.accessDenied", 40301),

    NOT_FOUND_RESOURCE("resource", 40401),
    NOT_FOUND_PAGE("page", 40402),
    NOT_FOUND_COMMON("common", 40403),

    UNSUPPORTED_MEDIA_TYPE("message.exception.unsupportedMediaType", 41501),

    UNPROCESSABLE_ENTITY("loginTaken", 42201),

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
