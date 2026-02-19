package com.studies.bookstore.application.port.in;

import com.studies.bookstore.application.model.PagedResult;
import com.studies.bookstore.domain.model.Book;

public interface GetBookUseCase {

    Book getBook(Long id);

    PagedResult<Book> getAllBooks(int page, int size);

}
