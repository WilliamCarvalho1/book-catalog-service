package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @NotBlank(message = "title must not be blank")
        String title,

        @NotBlank(message = "author must not be blank")
        String author,

        @NotBlank(message = "category must not be blank")
        String category,

        @NotNull(message = "price must not be null")
        @Positive(message = "price must be positive")
        BigDecimal price,

        @Positive(message = "publicationYear must be positive")
        int publicationYear,

        @PositiveOrZero(message = "quantity must be zero or positive")
        int quantity
) {
}
