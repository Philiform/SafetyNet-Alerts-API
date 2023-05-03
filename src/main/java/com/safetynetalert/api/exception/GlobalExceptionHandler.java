package com.safetynetalert.api.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> invalidDataException(InvalidDataException ex, WebRequest request) {
		CommonErrorHandler commonErrorHandler = new CommonErrorHandler();

        List<FieldError> errors = ex.getResult().getFieldErrors();

        for (FieldError error : errors) {
            logger.error("Filed Name ::: " + error.getField() + "Error Message :::" + error.getDefaultMessage());
        }

        return commonErrorHandler.fieldErrorResponse("Error", commonErrorHandler.getFieldErrorResponse(ex.getResult()));
	}

}