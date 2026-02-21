package com.studies.bookstore.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartTest {

    @Test
    void addItemShouldAggregateQuantityForSameBook() {
        ShoppingCart cart = new ShoppingCart("user1");
        CartItem item1 = new CartItem(1L, "Book 1", BigDecimal.TEN, 1);
        CartItem item2 = new CartItem(1L, "Book 1", BigDecimal.TEN, 2);

        cart.addItem(item1);
        cart.addItem(item2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(3);
    }

    @Test
    void getTotalPriceShouldSumSubtotals() {
        ShoppingCart cart = new ShoppingCart("user1");
        cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 2));
        cart.addItem(new CartItem(2L, "Book 2", new BigDecimal("5.00"), 1));

        assertThat(cart.getTotalPrice()).isEqualByComparingTo(new BigDecimal("25.00"));
    }
}