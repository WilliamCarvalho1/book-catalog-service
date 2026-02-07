package com.studies.bookcatalog.application.port.in;

import com.studies.bookcatalog.domain.model.Book;

public interface AddBookUseCase {

    Book addBook(Book book);
}
