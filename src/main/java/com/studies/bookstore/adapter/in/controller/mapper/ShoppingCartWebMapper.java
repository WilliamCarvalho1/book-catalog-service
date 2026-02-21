package com.studies.bookstore.adapter.in.controller.mapper;

import com.studies.bookstore.adapter.in.controller.dto.CartItemResponseDTO;
import com.studies.bookstore.adapter.in.controller.dto.ShoppingCartResponseDTO;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;

import java.util.List;

public final class ShoppingCartWebMapper {

    private ShoppingCartWebMapper() {
    }

    public static ShoppingCartResponseDTO toResponseDTO(ShoppingCart cart) {
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(ShoppingCartWebMapper::toItemResponseDTO)
                .toList();

        return new ShoppingCartResponseDTO(
                cart.getUserId(),
                items,
                cart.getTotalPrice(),
                cart.getItemCount(),
                cart.getTotalQuantity()
        );
    }

    private static CartItemResponseDTO toItemResponseDTO(CartItem item) {
        return new CartItemResponseDTO(
                item.getBookId(),
                item.getTitle(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}