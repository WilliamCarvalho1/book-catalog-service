package com.studies.domain.ports.input;

import com.studies.domain.dto.BookDTO;

public interface UpdateBookUseCase {

    BookDTO updateBook(String id, BookDTO bookDTO);

}
