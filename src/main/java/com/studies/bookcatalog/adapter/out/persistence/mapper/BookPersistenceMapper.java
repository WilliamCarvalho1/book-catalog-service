package com.studies.bookcatalog.adapter.out.persistence.mapper;

import com.studies.bookcatalog.adapter.out.persistence.entity.JpaBookEntity;
import com.studies.bookcatalog.domain.model.Book;

import java.util.List;

public class BookPersistenceMapper {

    private BookPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Book toDomain(JpaBookEntity jpaBookEntity) {
        return new Book(
                jpaBookEntity.id,
                jpaBookEntity.name,
                jpaBookEntity.author,
                jpaBookEntity.category,
                jpaBookEntity.price,
                jpaBookEntity.quantity
        );
    }

    public static JpaBookEntity toJpaEntity(Book book) {
        return new JpaBookEntity(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getQuantity()
        );
    }

    public static List<Book> toDomainList(List<JpaBookEntity> entities) {
        return entities.stream()
                .map(BookPersistenceMapper::toDomain)
                .toList();
    }
}
