package com.qurasense.userApi.controllers;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qurasense.common.ErrorInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ErrorInfo notFountGlobal(HttpServletRequest request, HttpServletResponse response, Exception ex, Locale locale) {
        logger.error("Request={} {}, principal={} => {}", new Object[]{
                request.getMethod(),
                request.getRequestURI(),
                request.getUserPrincipal(),
                ex.getMessage()
        });
        logger.error(ex.getMessage(), ex);


        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(StringUtils.isNoneBlank(ex.getMessage()) ? ex.getMessage() : ex.getClass().getSimpleName());
        return errorInfo;
    }

}