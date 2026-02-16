package com.studies.bookcatalog.application.service;

import com.studies.bookcatalog.application.exception.InvalidRequestException;
import com.studies.bookcatalog.application.exception.RequestNotFoundException;
import com.studies.bookcatalog.application.model.PagedResult;
import com.studies.bookcatalog.application.port.command.PartialUpdateBookCommand;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;
import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.exception.DomainException;
import com.studies.bookcatalog.domain.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

public class BookService implements AddBookUseCase, GetBookUseCase, UpdateBookUseCase, DeleteBookUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepositoryPort repository;

    private static final String BD_ERROR_MSG = "Database error: ";

    public BookService(BookRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Book addBook(Book request) {
        logger.info("Adding new book: title='{}', author='{}'", request.getTitle(), request.getAuthor());
        try {
            Book saved = repository.save(request);
            logger.info("Book added successfully with id={}", saved.getId());
            return saved;
        } catch (DataAccessException ex) {
            logger.error("Error while adding book '{}': {}", request.getTitle(), ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Book getBook(Long id) {
        if (id == null) {
            logger.warn("Attempted to get book with null id");
            throw new InvalidRequestException("Book id must be provided.");
        }

        try {
            Book book = repository.findById(id)
                    .orElseThrow(() -> new RequestNotFoundException(id));
            logger.info("Retrieved book with id={}", id);
            return book;
        } catch (RequestNotFoundException ex) {
            logger.warn("Book not found with id={}", id);
            throw ex;
        } catch (DataAccessException ex) {
            logger.error("Error while retrieving book with id={}: {}", id, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public PagedResult<Book> getAllBooks(int page, int size) {
        if (page < 0) {
            logger.warn("Invalid page index: {}", page);
            throw new InvalidRequestException("Page index must not be negative.");
        }
        if (size <= 0) {
            logger.warn("Invalid page size: {}", size);
            throw new InvalidRequestException("Page size must be greater than zero.");
        }

        try {
            PagedResult<Book> result = repository.findAll(page, size);
            logger.info("Retrieved {} books for page={}, size={}",
                    result.getContent().size(), page, size);
            return result;
        } catch (DataAccessException ex) {
            logger.error("Error while retrieving books for page={}, size={}: {}", page, size, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Book updateBook(Long id, UpdateBookCommand command) {
        try {
            logger.info("Updating book id={} with full update command", id);
            Book retrievedBook = getBook(id);
            retrievedBook.updateBook(command);
            Book updated = repository.update(retrievedBook);
            logger.info("Book id={} updated successfully", id);
            return updated;
        } catch (DomainException ex) {
            logger.warn("Domain validation failed when updating book id={}: {}", id, ex.getMessage());
            throw new InvalidRequestException(ex.getMessage());
        } catch (DataAccessException ex) {
            logger.error("Error while updating book id={}: {}", id, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Book partialUpdateBook(Long id, PartialUpdateBookCommand updateBookCommand) {
        try {
            logger.info("Partially updating book id={} with command", id);
            Book retrievedBook = getBook(id);
            retrievedBook.partialUpdateBook(updateBookCommand);
            Book updated = repository.update(retrievedBook);
            logger.info("Book id={} partially updated successfully", id);
            return updated;
        } catch (DomainException ex) {
            logger.warn("Domain validation failed when partially updating book id={}: {}", id, ex.getMessage());
            throw new InvalidRequestException(ex.getMessage());
        } catch (DataAccessException ex) {
            logger.error("Error while partially updating book id={}: {}", id, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteBook(Long id) {
        getBook(id);

        try {
            logger.info("Deleting book id={}", id);
            repository.deleteById(id);
            logger.info("Book id={} deleted successfully", id);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting book id={}: {}", id, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

}
