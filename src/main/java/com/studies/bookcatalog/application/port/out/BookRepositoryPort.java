package com.studies.bookcatalog.application.port.out;

import com.studies.bookcatalog.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    Book save(Book book);

    Optional<Book> findById(Long id);

    Optional<List<Book>> findAll();

    Book update(Book updatedBook);

    void deleteById(Long id);
}