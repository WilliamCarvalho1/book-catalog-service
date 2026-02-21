package com.studies.bookstore.adapter.in.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record ShoppingCartResponseDTO(
        String userId,
        List<CartItemResponseDTO> items,
        BigDecimal total,
        int itemCount,
        int totalQuantity
) {
}