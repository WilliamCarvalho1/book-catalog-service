package com.studies.bookcatalog.application.service;

import com.studies.bookcatalog.application.exception.InvalidRequestException;
import com.studies.bookcatalog.application.exception.RequestNotFoundException;
import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.model.Book;
import com.studies.bookcatalog.domain.model.BookUpdate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements AddBookUseCase, GetBookUseCase, UpdateBookUseCase, DeleteBookUseCase {

    private final BookRepositoryPort repository;

    private static final String BD_ERROR_MSG = "Database error: ";

    public BookService(BookRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Book addBook(Book request) {
        return repository.save(request);
    }

    @Override
    public Book getBook(Long id) {
        if (id == null) {
            throw new InvalidRequestException("Book id must be provided.");
        }

        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RequestNotFoundException(id));
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public List<Book> getAllBooks() {
        try {
            List<Book> books = repository.findAll().orElse(List.of());

            return books.isEmpty() ? List.of() : books;
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    public Book updateBook(Long id, BookUpdate request) {
        if (request.getPrice() == null && request.getAmount() == null) {
            throw new InvalidRequestException("At least one field (price or amount) must be provided for update.");
        }

        try {
            Book retrievedBook = getBook(id);

            Optional.ofNullable(request.getPrice())
                    .ifPresent(retrievedBook::setPrice);

            Optional.ofNullable(request.getAmount())
                    .ifPresent(retrievedBook::setAmount);

            return repository.update(retrievedBook);
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteBook(Long id) {
        Book retrievedBook = getBook(id);

        try {
            repository.deleteById(retrievedBook.getId());
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

}
