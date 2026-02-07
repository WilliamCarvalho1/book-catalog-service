package com.studies.bookcatalog.domain.model;

import com.studies.bookcatalog.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String name;
    private String author;
    private String category;
    private BigDecimal price;
    private int amount;

    public Book(String name, String author, String category, BigDecimal price, int amount) {
        changeName(name);
        changeAuthor(author);
        changeCategory(category);
        changePrice(price);
        changeAmount(amount);
    }

    public void changeName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Book name must not be blank.");
        }
        this.name = name;
    }

    public void changeAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new DomainException("Book author must not be blank.");
        }
        this.author = author;
    }

    public void changeCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new DomainException("Book category must not be blank.");
        }
        this.category = category;
    }

    public void changePrice(BigDecimal price) {
        if (price == null) {
            throw new DomainException("Book price must not be null.");
        }
        if (price.signum() <= 0) {
            throw new DomainException("Book price must be positive.");
        }
        this.price = price;
    }

    public void changeAmount(int amount) {
        if (amount < 0) {
            throw new DomainException("Book amount must not be negative.");
        }
        this.amount = amount;
    }

    public void applyUpdate(BookUpdate update) {
        Objects.requireNonNull(update, "BookUpdate must not be null");

        if (update.getPrice() == null && update.getAmount() == null) {
            throw new DomainException("At least one field (price or amount) must be provided for update.");
        }

        if (update.getPrice() != null) {
            changePrice(update.getPrice());
        }

        if (update.getAmount() != null) {
            changeAmount(update.getAmount());
        }
    }
}
