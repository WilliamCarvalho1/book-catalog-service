package com.studies.bookcatalog.application.port.out;

import com.studies.bookcatalog.application.model.PagedResult;
import com.studies.bookcatalog.domain.model.Book;

import java.util.Optional;

public interface BookRepositoryPort {
    Book save(Book book);

    Optional<Book> findById(Long id);

    PagedResult<Book> findAll(int page, int size);

    Book update(Book updatedBook);

    void deleteById(Long id);
}