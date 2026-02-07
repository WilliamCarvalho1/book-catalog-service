package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookRequestDTO(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String author,
        @NotNull @NotBlank String category,
        @NotNull @Positive BigDecimal price,
        @Positive int amount
) {
}