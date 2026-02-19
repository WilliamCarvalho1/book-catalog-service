package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.Book;

public interface AddBookUseCase {

    Book addBook(Book book);
}
