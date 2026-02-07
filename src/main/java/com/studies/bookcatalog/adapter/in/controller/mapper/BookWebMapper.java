package com.studies.bookcatalog.adapter.in.controller.mapper;

import com.studies.bookcatalog.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookcatalog.domain.model.Book;

import java.util.List;

public class BookWebMapper {

    private BookWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Book toDomain(BookRequestDTO requestDTO) {

        return new Book(
            requestDTO.name(),
            requestDTO.author(),
            requestDTO.category(),
            requestDTO.price(),
            requestDTO.quantity()
        );
    }

    public static BookResponseDTO toResponseDTO(Book book) {

        return new BookResponseDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getQuantity()
        );
    }


    public static List<BookResponseDTO> toResponseDTOList(List<Book> allBooks) {
        return allBooks.stream()
                .map(BookWebMapper::toResponseDTO)
                .toList();
    }
}
