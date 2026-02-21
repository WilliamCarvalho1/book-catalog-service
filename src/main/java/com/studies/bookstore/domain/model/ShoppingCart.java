package com.studies.bookstore.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ShoppingCart {

    private final String userId;
    private final List<CartItem> items;

    public ShoppingCart(String userId) {
        this(userId, new ArrayList<>());
    }

    public ShoppingCart(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void addItem(CartItem newItem) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getBookId().equals(newItem.getBookId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().increaseQuantity(newItem.getQuantity());
        } else {
            items.add(newItem);
        }
    }

    public void updateItemQuantity(Long bookId, int quantity) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getBookId().equals(bookId))
                .findFirst();
        existing.ifPresent(item -> {
            if (quantity <= 0) {
                items.remove(item);
            } else {
                item.setQuantity(quantity);
            }
        });
    }

    public void removeItem(Long bookId) {
        items.removeIf(i -> i.getBookId().equals(bookId));
    }

    public void clear() {
        items.clear();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

}