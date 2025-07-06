package com.studies.adapters.output.repository;

import com.studies.adapters.entity.BookEntity;
import com.studies.domain.dto.BookDTO;
import com.studies.domain.ports.output.CatalogRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.studies.adapters.mapper.BookMapper.*;

@Repository
public class CatalogRepository implements CatalogRepositoryPort {

    private final SpringCatalogRepository repository;

    public CatalogRepository(SpringCatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        BookEntity response = repository.save(mapToEntity(bookDTO));

        return mapToDTO(response);
    }

    @Override
    public BookDTO findById(String bookId) {
        BookEntity entity = repository.findById(bookId).orElseThrow();

        return mapToDTO(entity);
    }

    @Override
    public List<BookDTO> findAll() {
        List<BookEntity> entityList = repository.findAll();

        return mapToDTOList(entityList);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {
        BookEntity entity = repository.save(mapToEntity(bookDTO));

        return mapToDTO(entity);
    }

    @Override
    public void deleteBook(String bookId) {
        repository.deleteById(bookId);
    }

}
