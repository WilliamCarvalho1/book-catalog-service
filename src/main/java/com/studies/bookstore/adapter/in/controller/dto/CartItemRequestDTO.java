package com.studies.bookstore.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDTO(

        @NotNull(message = "bookId must not be null")
        Long bookId,
        @Positive(message = "publicationYear must be positive")
        int quantity
) {
}