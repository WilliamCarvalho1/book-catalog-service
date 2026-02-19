package com.studies.bookstore.adapter.out.persistence.mapper;

import com.studies.bookstore.adapter.out.persistence.entity.JpaBookEntity;
import com.studies.bookstore.domain.model.Book;

import java.util.List;

public class BookPersistenceMapper {

    private BookPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Book toDomain(JpaBookEntity jpaBookEntity) {
        return new Book(
                jpaBookEntity.id,
                jpaBookEntity.title,
                jpaBookEntity.author,
                jpaBookEntity.category,
                jpaBookEntity.price,
                jpaBookEntity.publicationYear,
                jpaBookEntity.quantity
        );
    }

    public static JpaBookEntity toJpaEntity(Book book) {
        return new JpaBookEntity(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getPublicationYear(),
                book.getQuantity()
        );
    }

    public static List<Book> toDomainList(List<JpaBookEntity> entities) {
        return entities.stream()
                .map(BookPersistenceMapper::toDomain)
                .toList();
    }
}
