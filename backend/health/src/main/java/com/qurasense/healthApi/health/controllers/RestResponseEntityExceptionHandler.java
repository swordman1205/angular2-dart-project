package com.qurasense.healthApi.controllers;

import com.qurasense.common.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Locale;

//@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ErrorInfo handleSecurity(HttpServletRequest request, HttpServletResponse response, Exception ex, Locale locale) {
        logger.error("Request={} {}, principal={} => {}", new Object[]{
                request.getMethod(),
                request.getRequestURI(),
                request.getUserPrincipal(),
                ex.getMessage()
        });

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage("denied access this resource");
        return errorInfo;
    }
    /**
     * @param request
     * @param response
     * @return
     */

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    @ResponseBody
    public ErrorInfo handleInternal(HttpServletRequest request, HttpServletResponse response, Exception ex, Locale locale) {
        logger.error("Request={} {}, principal={} => {}", new Object[]{
                request.getMethod(),
                request.getRequestURI(),
                request.getUserPrincipal(),
                ex.getMessage()
        });

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(ex.getMessage());
        return errorInfo;
    }

}