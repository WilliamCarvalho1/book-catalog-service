package com.studies.bookstore.application.port.in;

import com.studies.bookstore.application.port.command.PartialUpdateBookCommand;
import com.studies.bookstore.domain.model.Book;
import com.studies.bookstore.application.port.command.UpdateBookCommand;

public interface UpdateBookUseCase {

    Book updateBook(Long id, UpdateBookCommand updateBookCommand);

    Book partialUpdateBook(Long id, PartialUpdateBookCommand updateBookCommand);

}
