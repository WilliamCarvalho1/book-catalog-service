package com.studies.bookstore.adapter.in.controller.error;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final ErrorCode code;
    private final String message;

    public ApiErrorResponse(
            int status,
            String error,
            ErrorCode code,
            String message
    ) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

}
