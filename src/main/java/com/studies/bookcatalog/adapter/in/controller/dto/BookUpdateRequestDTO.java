package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @NotNull(message = "price must not be null")
        @Positive(message = "price must be positive")
        BigDecimal price,

        @NotNull(message = "quantity must not be null")
        @Positive(message = "quantity must be positive")
        Integer quantity
) {
}
