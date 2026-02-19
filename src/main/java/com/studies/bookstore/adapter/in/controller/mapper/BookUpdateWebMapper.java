package com.studies.bookstore.adapter.in.controller.mapper;

import com.studies.bookstore.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookstore.application.port.command.PartialUpdateBookCommand;
import com.studies.bookstore.application.port.command.UpdateBookCommand;

import java.util.Optional;

public class BookUpdateWebMapper {

    private BookUpdateWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static UpdateBookCommand toDomain(BookRequestDTO requestDTO) {

        return new UpdateBookCommand(
                Optional.ofNullable(requestDTO.title()),
                Optional.ofNullable(requestDTO.author()),
                Optional.ofNullable(requestDTO.category()),
                Optional.ofNullable(requestDTO.price()),
                Optional.of(requestDTO.publicationYear()),
                Optional.of(requestDTO.quantity())
        );
    }

    public static PartialUpdateBookCommand toDomain(BookUpdateRequestDTO requestDTO) {

        return new PartialUpdateBookCommand(
                Optional.ofNullable(requestDTO.price()),
                Optional.ofNullable(requestDTO.quantity())
        );
    }
}
