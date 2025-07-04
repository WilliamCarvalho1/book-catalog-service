package com.studies.adapters.output.mongo_repository;

import com.studies.adapters.entity.BookEntity;
import com.studies.domain.dto.BookDTO;
import com.studies.domain.ports.output.CatalogRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoCatalogRepository implements CatalogRepositoryPort {

    private final SpringMongoCatalogRepository repository;

    public MongoCatalogRepository(SpringMongoCatalogRepository repository) {
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
    public void deleteBook(BookDTO bookDTO) {
        repository.deleteById(bookDTO.getId());
    }

    private BookEntity mapToEntity(BookDTO dto) {
        return BookEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .author(dto.getAuthor())
                .category(dto.getCategory())
                .status(dto.getStatus())
                .price(dto.getPrice())
                .build();
    }

    private BookDTO mapToDTO(BookEntity entity) {
        return BookDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .author(entity.getAuthor())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .price(entity.getPrice())
                .build();
    }

    private List<BookDTO> mapToDTOList(List<BookEntity> entityList) {
        return entityList.stream()
                .map(this::mapToDTO)
                .toList();
    }

}
