package com.studies.bookstore.application.port.out;

import com.studies.bookstore.application.model.PagedResult;
import com.studies.bookstore.domain.model.Book;

import java.util.Optional;

public interface BookRepositoryPort {
    Book save(Book book);

    Optional<Book> findById(Long id);

    PagedResult<Book> findAll(int page, int size);

    Book update(Book updatedBook);

    void deleteById(Long id);
}