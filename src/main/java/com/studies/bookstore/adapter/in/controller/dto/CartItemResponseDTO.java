package com.studies.bookstore.adapter.in.controller.dto;

import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long bookId,
        String title,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {
}