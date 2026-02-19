package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.adapter.out.persistence.entity.JpaBookEntity;
import com.studies.bookstore.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.studies.bookstore.application.model.PagedResult;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.domain.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final JpaBookRepository repository;

    public BookRepositoryAdapter(JpaBookRepository repository) {
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
    public PagedResult<Book> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaBookEntity> pageResult = repository.findAll(pageable);

        List<Book> content = BookPersistenceMapper.toDomainList(pageResult.getContent());

        return new PagedResult<>(
                content,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
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
