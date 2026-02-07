package com.studies.bookcatalog.adapter.out.persistence;

import com.studies.bookcatalog.adapter.out.persistence.entity.JpaBookEntity;
import com.studies.bookcatalog.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final TransferJpaRepository repository;

    public BookRepositoryAdapter(TransferJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        JpaBookEntity entity = BookPersistenceMapper.toJpaEntity(book);

        JpaBookEntity savedEntity = repository.save(entity);

        return BookPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id)
                .map(BookPersistenceMapper::toDomain);
    }

    @Override
    public Optional<List<Book>> findAll() {
        List<JpaBookEntity> entities = repository.findAll();

        return Optional.of(BookPersistenceMapper.toDomainList(entities));
    }

    @Override
    @Transactional
    public Book update(Book retrievedBook) {
        JpaBookEntity savedEntity = repository.save(
                BookPersistenceMapper.toJpaEntity(retrievedBook)
        );

        return BookPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
