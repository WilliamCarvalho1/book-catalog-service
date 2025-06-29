package com.studies.domain.ports.output;

import com.studies.domain.dto.BookDTO;

import java.util.List;

public interface CatalogRepositoryPort {

    BookDTO saveBook(BookDTO bookDTO);

    BookDTO findById(String bookId);

    List<BookDTO> findAll();

    BookDTO updateBook(BookDTO bookDTO);

    void deleteBook(BookDTO bookDTO);
}
