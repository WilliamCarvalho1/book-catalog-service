package com.studies.bookcatalog.application.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long id) {
        super("Request not found with id " + id);
    }
}
