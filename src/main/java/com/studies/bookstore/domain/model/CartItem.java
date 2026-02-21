package com.studies.bookstore.domain.model;

import com.studies.bookstore.domain.exception.DomainException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartItem {

    private final Long bookId;
    private final String title;
    private final BigDecimal unitPrice;
    private Integer quantity;

    public CartItem(Long bookId, String title, BigDecimal unitPrice, Integer quantity) {
        if (bookId == null) {
            throw new DomainException("Book id cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new DomainException("Title cannot be empty");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Unit price must be greater than 0");
        }
        if (quantity == null || quantity <= 0) {
            throw new DomainException("Quantity must be greater than 0");
        }
        this.bookId = bookId;
        this.title = title.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new DomainException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public void increaseQuantity(int delta) {
        int newQuantity = this.quantity + delta;
        if (newQuantity <= 0) {
            throw new DomainException("Quantity must be greater than 0");
        }
        this.quantity = newQuantity;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

}