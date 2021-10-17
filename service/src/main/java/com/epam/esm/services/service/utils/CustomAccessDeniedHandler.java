package com.epam.esm.services.service.utils;

import com.epam.esm.services.exceptions.ErrorResponse;
import com.epam.esm.services.exceptions.ExceptionMessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LogManager.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        Locale locale = request.getLocale();
        String errorMessage = ErrorResponse.getMessageForLocale(ExceptionMessageType.FORBIDDEN.getMessageKey(), locale);
        int errorCode = ExceptionMessageType.FORBIDDEN.getErrorCode();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{ \n\"errorCode\" : \"" + errorMessage);
        response.getWriter().write("\",\n    \"errorMessage\" : \"" + errorCode + "\"\n}");
        LOGGER.info("User: " + authentication.getName() + " attempted to access the protected URL: " + request.getRequestURI());
    }

}
