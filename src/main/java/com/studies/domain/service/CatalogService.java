package com.studies.domain.service;

import com.studies.domain.dto.BookDTO;
import com.studies.domain.ports.input.AddBookUseCase;
import com.studies.domain.ports.input.DeleteBookUseCase;
import com.studies.domain.ports.input.GetBookUseCase;
import com.studies.domain.ports.input.UpdateBookUseCase;
import com.studies.domain.ports.output.CatalogRepositoryPort;

import java.util.List;

public class CatalogService implements AddBookUseCase, GetBookUseCase, UpdateBookUseCase, DeleteBookUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;

    public CatalogService(CatalogRepositoryPort catalogRepositoryPort) {
        this.catalogRepositoryPort = catalogRepositoryPort;
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        return catalogRepositoryPort.saveBook(bookDTO);
    }

    @Override
    public BookDTO getBook(String id) {
        return catalogRepositoryPort.findById(id);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return catalogRepositoryPort.findAll();
    }

    @Override
    public BookDTO updateBook(String id, BookDTO bookDTO) {

        BookDTO existingBook = getBook(id);

        if (existingBook != null) {
            existingBook.setName(bookDTO.getName());
            existingBook.setAuthor(bookDTO.getAuthor());
            existingBook.setCategory(bookDTO.getCategory());
            existingBook.setStatus(bookDTO.getStatus());
            existingBook.setPrice(bookDTO.getPrice());
        }

        return catalogRepositoryPort.updateBook(existingBook);
    }

    @Override
    public void deleteBook(String id) {
        BookDTO bookDTO = getBook(id);
        catalogRepositoryPort.deleteBook(bookDTO.getId());
    }
}