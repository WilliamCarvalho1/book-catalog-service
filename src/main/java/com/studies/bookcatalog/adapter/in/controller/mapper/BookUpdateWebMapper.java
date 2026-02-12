package com.studies.bookcatalog.adapter.in.controller.mapper;

import com.studies.bookcatalog.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;

import java.util.Optional;

public class BookUpdateWebMapper {

    private BookUpdateWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static UpdateBookCommand toDomain(BookUpdateRequestDTO requestDTO) {

        return new UpdateBookCommand(
                Optional.ofNullable(requestDTO.title()),
                Optional.ofNullable(requestDTO.author()),
                Optional.ofNullable(requestDTO.category()),
                Optional.ofNullable(requestDTO.price()),
                Optional.of(requestDTO.publicationYear()),
                Optional.of(requestDTO.quantity())
        );
    }
}
