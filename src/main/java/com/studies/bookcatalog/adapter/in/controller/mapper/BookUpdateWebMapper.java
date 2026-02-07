package com.studies.bookcatalog.adapter.in.controller.mapper;

import com.studies.bookcatalog.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookcatalog.domain.model.BookUpdate;

public class BookUpdateWebMapper {

    private BookUpdateWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BookUpdate toDomain(BookUpdateRequestDTO requestDTO) {

        return new BookUpdate(
                requestDTO.price(),
                requestDTO.amount()
        );
    }
}
