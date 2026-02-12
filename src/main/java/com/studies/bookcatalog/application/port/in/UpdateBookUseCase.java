package com.studies.bookcatalog.application.port.in;

import com.studies.bookcatalog.domain.model.Book;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;

public interface UpdateBookUseCase {

    Book updateBook(Long id, UpdateBookCommand updateBookCommand);

}
