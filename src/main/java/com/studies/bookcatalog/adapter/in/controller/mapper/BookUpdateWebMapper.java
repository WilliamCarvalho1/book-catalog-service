package com.studies.bookcatalog.adapter.in.controller.mapper;

import com.studies.bookcatalog.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookcatalog.domain.model.BookUpdate;

import java.util.Optional;

public class BookUpdateWebMapper {

    private BookUpdateWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BookUpdate toDomain(BookUpdateRequestDTO requestDTO) {

        return new BookUpdate(
                Optional.ofNullable(requestDTO.title()),
                Optional.ofNullable(requestDTO.author()),
                Optional.ofNullable(requestDTO.category()),
                Optional.ofNullable(requestDTO.price()),
                Optional.of(requestDTO.publicationYear()),
                Optional.of(requestDTO.quantity())
        );
    }
}
