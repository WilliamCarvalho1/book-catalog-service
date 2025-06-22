package com.studies.domain.ports.input;

import com.studies.domain.dto.BookDTO;

public interface AddBookUseCase {

    BookDTO addBook(BookDTO bookDTO);
}
