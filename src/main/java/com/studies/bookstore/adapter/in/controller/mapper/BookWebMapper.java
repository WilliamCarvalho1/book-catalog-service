package com.studies.bookstore.adapter.in.controller.mapper;

import com.studies.bookstore.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookstore.domain.model.Book;

import java.util.List;

public class BookWebMapper {

    private BookWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Book toDomain(BookRequestDTO requestDTO) {

        return new Book(
                null,
                requestDTO.title(),
                requestDTO.author(),
                requestDTO.category(),
                requestDTO.price(),
                requestDTO.publicationYear(),
                requestDTO.quantity()
        );
    }

    public static BookResponseDTO toResponseDTO(Book book) {

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getPublicationYear(),
                book.getQuantity()
        );
    }


    public static List<BookResponseDTO> toResponseDTOList(List<Book> allBooks) {
        return allBooks.stream()
                .map(BookWebMapper::toResponseDTO)
                .toList();
    }
}
