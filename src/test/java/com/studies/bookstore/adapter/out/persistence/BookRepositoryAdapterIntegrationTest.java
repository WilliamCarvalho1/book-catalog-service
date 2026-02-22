package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.AbstractPostgresContainerTest;
import com.studies.bookstore.adapter.out.persistence.entity.JpaBookEntity;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.domain.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookRepositoryAdapter.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryAdapterIntegrationTest extends AbstractPostgresContainerTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private BookRepositoryPort bookRepository;

    @Test
    @DisplayName("should persist and load book using real JPA + Flyway schema")
    void shouldPersistAndLoadBook() {
        // given
        Book book = new Book(null, "Book 1", "Author", "Category",
                new BigDecimal("19.99"), 2024, 10);

        // when
        Book saved = bookRepository.save(book);

        // then - verify entity state
        Long id = saved.getId();
        JpaBookEntity entity = jpaBookRepository.findById(id).orElseThrow();
        assertThat(entity.title).isEqualTo("Book 1");
        assertThat(entity.author).isEqualTo("Author");
        assertThat(entity.category).isEqualTo("Category");
        assertThat(entity.price).isEqualByComparingTo(new BigDecimal("19.99"));
        assertThat(entity.publicationYear).isEqualTo(2024);
        assertThat(entity.quantity).isEqualTo(10);

        // and via port
        Book loaded = bookRepository.findById(id).orElseThrow();
        assertThat(loaded.getTitle()).isEqualTo("Book 1");
        assertThat(loaded.getAuthor()).isEqualTo("Author");
        assertThat(loaded.getCategory()).isEqualTo("Category");
        assertThat(loaded.getPrice()).isEqualByComparingTo(new BigDecimal("19.99"));
        assertThat(loaded.getPublicationYear()).isEqualTo(2024);
        assertThat(loaded.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("should update existing book when saving with same id")
    void shouldUpdateExistingBook() {
        // given - initial save
        Book original = new Book(null, "Original Title", "Original Author", "Category",
                new BigDecimal("9.99"), 2020, 5);
        Book saved = bookRepository.save(original);
        Long id = saved.getId();

        // sanity check
        JpaBookEntity initialEntity = jpaBookRepository.findById(id).orElseThrow();
        assertThat(initialEntity.title).isEqualTo("Original Title");
        assertThat(initialEntity.quantity).isEqualTo(5);

        // when - update the same book
        Book updated = new Book(id, "Updated Title", "Updated Author", "New Category",
                new BigDecimal("14.99"), 2021, 8);
        bookRepository.update(updated);

        // then - entity should reflect updated fields
        JpaBookEntity entity = jpaBookRepository.findById(id).orElseThrow();
        assertThat(entity.title).isEqualTo("Updated Title");
        assertThat(entity.author).isEqualTo("Updated Author");
        assertThat(entity.category).isEqualTo("New Category");
        assertThat(entity.price).isEqualByComparingTo(new BigDecimal("14.99"));
        assertThat(entity.publicationYear).isEqualTo(2021);
        assertThat(entity.quantity).isEqualTo(8);

        // and via port
        Book loaded = bookRepository.findById(id).orElseThrow();
        assertThat(loaded.getTitle()).isEqualTo("Updated Title");
        assertThat(loaded.getQuantity()).isEqualTo(8);
    }
}