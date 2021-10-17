package com.epam.esm.services.service.utils;

import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ExceptionMessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LogManager.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        Locale locale = request.getLocale();
        String errorMessageType = exception.getMessage();
        String errorMessage;
        int errorCode;
        if(errorMessageType != null && errorMessageType.contains("Bad credentials")){
            errorMessage = ErrorResponse.getMessageForLocale(ExceptionMessageType.UNKNOWN_USER.getMessageKey(), locale);
            errorCode = ExceptionMessageType.UNKNOWN_USER.getErrorCode();
        } else {
            errorMessage = ErrorResponse.getMessageForLocale(ExceptionMessageType.UNAUTHORIZED.getMessageKey(), locale);
            errorCode = ExceptionMessageType.UNAUTHORIZED.getErrorCode();
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{ \n\"errorCode\" : \"" + errorMessage);
        response.getWriter().write("\",\n    \"errorMessage\" : \"" + errorCode + "\"\n}");
        LOGGER.info("Unauthorized error: {}", exception.getLocalizedMessage());
    }

}
