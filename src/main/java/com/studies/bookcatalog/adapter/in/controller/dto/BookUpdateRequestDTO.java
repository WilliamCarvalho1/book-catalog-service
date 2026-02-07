package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @NotNull(message = "price must not be null")
        @Positive(message = "price must be positive")
        BigDecimal price,

        @NotNull(message = "amount must not be null")
        @Positive(message = "amount must be positive")
        Integer amount
) {
}
