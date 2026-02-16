package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @NotNull(message = "price must not be null")
        @Positive(message = "price must be positive")
        BigDecimal price,

        @PositiveOrZero(message = "quantity must be zero or positive")
        int quantity
) {
}
