package com.studies.bookcatalog.application.port.in;

import com.studies.bookcatalog.domain.model.Book;

import java.util.List;

public interface GetBookUseCase {

    Book getBook(Long id);

    List<Book> getAllBooks();

}
