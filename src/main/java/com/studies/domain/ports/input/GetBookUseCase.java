package com.studies.domain.ports.input;

import com.studies.domain.dto.BookDTO;

import java.util.List;

public interface GetBookUseCase {

    BookDTO getBook(String bookId);

    List<BookDTO> getAllBooks();

}
