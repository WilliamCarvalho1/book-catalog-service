package com.studies.bookcatalog.application.service;

import com.studies.bookcatalog.application.exception.InvalidRequestException;
import com.studies.bookcatalog.application.exception.RequestNotFoundException;
import com.studies.bookcatalog.application.model.PagedResult;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;
import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.exception.DomainException;
import com.studies.bookcatalog.domain.model.Book;
import org.springframework.dao.DataAccessException;

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
    public PagedResult<Book> getAllBooks(int page, int size) {
        if (page < 0) {
            throw new InvalidRequestException("Page index must not be negative.");
        }
        if (size <= 0) {
            throw new InvalidRequestException("Page size must be greater than zero.");
        }

        try {
            return repository.findAll(page, size);
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    public Book updateBook(Long id, UpdateBookCommand command) {
        try {
            Book retrievedBook = getBook(id);
            retrievedBook.updateBook(command);
            return repository.update(retrievedBook);
        } catch (DomainException ex) {
            throw new InvalidRequestException(ex.getMessage());
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteBook(Long id) {
        getBook(id);

        try {
            repository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

}
