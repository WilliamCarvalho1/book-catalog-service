package com.studies.bookcatalog.domain.model;

import com.studies.bookcatalog.application.port.command.PartialUpdateBookCommand;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;
import com.studies.bookcatalog.domain.exception.DomainException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Year;

@Getter
public class Book {
    private final Long id;
    private String title;
    private String author;
    private String category;
    private BigDecimal price;
    private Integer publicationYear;
    private Integer quantity;

    public Book(Long id, String title, String author, String category,
                BigDecimal price, Integer publicationYear, Integer quantity) {

        this.id = id;
        changeTitle(title);
        changeAuthor(author);
        changeCategory(category);
        changePrice(price);
        changePublicationYear(publicationYear);
        changeQuantity(quantity);
    }

    public void updateBook(UpdateBookCommand update) {
        update.title().ifPresent(this::changeTitle);
        update.author().ifPresent(this::changeAuthor);
        update.category().ifPresent(this::changeCategory);
        update.price().ifPresent(this::changePrice);
        update.publicationYear().ifPresent(this::changePublicationYear);
        update.quantity().ifPresent(this::changeQuantity);
    }

    public void partialUpdateBook(PartialUpdateBookCommand update) {
        update.price().ifPresent(this::changePrice);
        update.quantity().ifPresent(this::changeQuantity);
    }

    private void changeTitle(String title) {
        if (title.isBlank()) {
            throw new DomainException("Title cannot be empty");
        }
        this.title = title.trim();
    }

    private void changeAuthor(String author) {
        if (author.isBlank()) {
            throw new DomainException("Author cannot be empty");
        }
        this.author = author.trim();
    }

    private void changeCategory(String category) {
        if (category.isBlank()) {
            throw new DomainException("Category cannot be empty");
        }
        this.category = category.trim();
    }

    private void changePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Price must be greater than 0");
        }
        this.price = price;
    }

    private void changeQuantity(Integer quantity) {

        if (quantity < 0) {
            throw new DomainException("Quantity cannot be negative");
        }

        this.quantity = quantity;
    }

    private void changePublicationYear(Integer year) {
        int currentYear = Year.now().getValue();

        if (year < 1800 || year > currentYear) {
            throw new DomainException("Invalid publication year");
        }

        this.publicationYear = year;
    }
}
