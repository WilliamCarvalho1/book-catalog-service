package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @Positive(message = "price must be positive")
        BigDecimal price,

        @PositiveOrZero(message = "quantity must be zero or positive")
        Integer quantity
) {
}
