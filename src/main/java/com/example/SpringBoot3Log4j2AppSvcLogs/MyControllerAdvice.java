package com.example.SpringBoot3Log4j2AppSvcLogs;

import java.io.*;

import org.apache.commons.logging.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackageClasses = SpringBoot3Log4j2AppSvcLogsApplication.class)
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

    private Log logger = LogFactory.getLog(MyControllerAdvice.class);

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);
        logger.error(getStackTrace(ex));
		return new ResponseEntity<String>(ex.getMessage(), status);
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		HttpStatus status = HttpStatus.resolve(code);
		return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
	}

    // Return the stacktrace of a Throwable as a String
	private String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}