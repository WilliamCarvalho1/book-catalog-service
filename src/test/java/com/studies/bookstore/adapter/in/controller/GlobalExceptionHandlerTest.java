package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.error.ApiErrorResponse;
import com.studies.bookstore.adapter.in.controller.error.ErrorCode;
import com.studies.bookstore.application.exception.InvalidRequestException;
import com.studies.bookstore.application.exception.RequestNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleNotFound should map to 404 BOOK_NOT_FOUND")
    void handleNotFound() {
        ResponseEntity<ApiErrorResponse> response = handler.handleNotFound(new RequestNotFoundException(1L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.REQUEST_NOT_FOUND);
    }

    @Test
    @DisplayName("handleInvalidRequest should map to 400 INVALID_BOOK")
    void handleInvalid() {
        ResponseEntity<ApiErrorResponse> response = handler.handleInvalidRequest(new InvalidRequestException("msg"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleNoHandlerFound should map /api/v1/books to BAD_REQUEST")
    void handleNoHandlerFoundBooks() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/v1/books/", null);

        ResponseEntity<ApiErrorResponse> response = handler.handleNoHandlerFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleNoHandlerFound should map other paths to 404 BAD_REQUEST")
    void handleNoHandlerFoundOtherPaths() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/other", null);

        ResponseEntity<ApiErrorResponse> response = handler.handleNoHandlerFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.REQUEST_NOT_FOUND);
    }

    @Test
    @DisplayName("handleGeneric should map to 500 INTERNAL_ERROR")
    void handleGeneric() {
        ResponseEntity<ApiErrorResponse> response = handler.handleGeneric(new RuntimeException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.INTERNAL_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected error");
    }

    @Test
    @DisplayName("handleValidation should map field errors to VALIDATION_ERROR with details")
    void handleValidation() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "authRequest");
        bindingResult.addError(new FieldError("authRequest", "username", "username must not be blank"));
        bindingResult.addError(new FieldError("authRequest", "password", "password must not be null"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThat(response.getBody().getMessage())
                .contains("username: username must not be blank")
                .contains("password: password must not be null");
    }
}
