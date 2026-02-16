package com.studies.bookcatalog.application.port.in;

import com.studies.bookcatalog.application.model.PagedResult;
import com.studies.bookcatalog.domain.model.Book;

public interface GetBookUseCase {

    Book getBook(Long id);

    PagedResult<Book> getAllBooks(int page, int size);

}
