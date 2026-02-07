package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookUpdateRequestDTO(
        @Positive BigDecimal price,
        @Positive Integer amount
) {
}
