package com.studies.domain.ports.input;

import com.studies.domain.dto.BookDTO;

import java.util.List;

public interface GetBookUseCase {

    BookDTO getBook(int bookId);

    List<BookDTO> getAllBooks();

}
