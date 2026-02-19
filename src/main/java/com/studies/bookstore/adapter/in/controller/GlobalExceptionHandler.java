package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.error.ApiErrorResponse;
import com.studies.bookstore.adapter.in.controller.error.ErrorCode;
import com.studies.bookstore.application.exception.InvalidRequestException;
import com.studies.bookstore.application.exception.RequestNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RequestNotFoundException ex) {
        logger.warn("Handling RequestNotFoundException: {}", ex.getMessage());
        return build(
                HttpStatus.NOT_FOUND,
                ErrorCode.REQUEST_NOT_FOUND,
                ex
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
        logger.warn("Handling InvalidRequestException: {}", ex.getMessage());
        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCode.BAD_REQUEST,
                ex
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        logger.warn("NoHandlerFoundException for URL='{}'", ex.getRequestURL());
        if (ex.getRequestURL().startsWith("/api/v1/books/")) {
            return build(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.BAD_REQUEST,
                    "ID must not be null."
            );
        }
        return build(
                HttpStatus.NOT_FOUND,
                ErrorCode.REQUEST_NOT_FOUND,
                "Resource not found."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        logger.error("Unexpected exception caught in GlobalExceptionHandler", ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_ERROR,
                "Unexpected error"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        String message = details.isBlank() ? "Request validation failed" : details;
        logger.warn("Validation failed: {}", message);

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                message
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, ErrorCode code, Exception ex) {
        return build(
                status,
                code,
                ex.getMessage()
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, ErrorCode code, String message) {
        logger.debug("Building ApiErrorResponse: status={}, code={}, message='{}'", status, code, message);
        return ResponseEntity
                .status(status)
                .body(
                        new ApiErrorResponse(
                                status.value(),
                                status.getReasonPhrase(),
                                code,
                                message
                        )
                );
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}
