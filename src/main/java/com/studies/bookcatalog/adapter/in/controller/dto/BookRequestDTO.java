package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookRequestDTO(
        @NotNull(message = "name must not be null")
        @NotBlank(message = "name must not be blank")
        String name,

        @NotNull(message = "author must not be null")
        @NotBlank(message = "author must not be blank")
        String author,

        @NotNull(message = "category must not be null")
        @NotBlank(message = "category must not be blank")
        String category,

        @NotNull(message = "price must not be null")
        @Positive(message = "price must be positive")
        BigDecimal price,

        @Positive(message = "quantity must be positive")
        int quantity
) {
}