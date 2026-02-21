package com.studies.bookstore.adapter.in.controller.dto;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateRequestDTO(
        @Positive(message = "quantity must be positive")
        int quantity
) {
}