package com.studies.bookcatalog.application.service;

import com.studies.bookcatalog.application.exception.InvalidRequestException;
import com.studies.bookcatalog.application.exception.RequestNotFoundException;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.model.Book;
import com.studies.bookcatalog.domain.model.BookUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private final BookRepositoryPort repository = Mockito.mock(BookRepositoryPort.class);
    private final BookService service = new BookService(repository);

    @Test
    @DisplayName("addBook should delegate to repository and return saved entity")
    void addBookShouldDelegateToRepository() {
        Book request = new Book();
        Book saved = new Book("Book 1", "Author", "Category", BigDecimal.TEN, 5);

        when(repository.save(request)).thenReturn(saved);

        Book result = service.addBook(request);

        assertThat(result).isSameAs(saved);
        verify(repository).save(request);
    }

    @Nested
    @DisplayName("getBook")
    class GetBook {

        @Test
        @DisplayName("should throw InvalidRequestException when id is null")
        void shouldThrowWhenIdIsNull() {
            assertThatThrownBy(() -> service.getBook(null))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("Book id must be provided.");
        }

        @Test
        @DisplayName("should throw RequestNotFoundException when book does not exist")
        void shouldThrowWhenBookNotFound() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getBook(1L))
                    .isInstanceOf(RequestNotFoundException.class);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException")
        void shouldWrapDataAccessExceptionOnGet() {
            when(repository.findById(1L)).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.getBook(1L))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }

        @Test
        @DisplayName("should return book when found")
        void shouldReturnBookWhenFound() {
            Book book = new Book("Book 1", "Author", "Category", BigDecimal.TEN, 5);

            when(repository.findById(1L)).thenReturn(Optional.of(book));

            Book result = service.getBook(1L);

            assertThat(result).isSameAs(book);
        }
    }

    @Test
    @DisplayName("getAllBooks should return empty list when repository returns empty")
    void getAllBooksEmpty() {
        when(repository.findAll()).thenReturn(Optional.of(List.of()));

        List<Book> result = service.getAllBooks();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAllBooks should return books when repository returns list")
    void getAllBooksNonEmpty() {
        Book book1 = new Book("Book 1", "Author 1", "Category 1", BigDecimal.ONE, 1);
        Book book2 = new Book("Book 2", "Author 2", "Category 2", BigDecimal.TEN, 2);

        when(repository.findAll()).thenReturn(Optional.of(List.of(book1, book2)));

        List<Book> result = service.getAllBooks();

        assertThat(result).containsExactly(book1, book2);
    }

    @Test
    @DisplayName("getAllBooks should return empty list when repository returns Optional.empty")
    void getAllBooksOptionalEmpty() {
        when(repository.findAll()).thenReturn(Optional.empty());

        List<Book> result = service.getAllBooks();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAllBooks should wrap DataAccessException into InvalidRequestException")
    void getAllBooksDataAccessException() {
        when(repository.findAll()).thenThrow(new DataAccessException("db error") {
        });

        assertThatThrownBy(service::getAllBooks)
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Database error:");
    }

    @Nested
    @DisplayName("updateBook")
    class UpdateBook {

        @Test
        @DisplayName("should throw InvalidRequestException when both price and quantity are null")
        void shouldThrowWhenNoFieldsProvided() {
            BookUpdate update = new BookUpdate();

            Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 1);
            when(repository.findById(1L)).thenReturn(Optional.of(existing));

            assertThatThrownBy(() -> service.updateBook(1L, update))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("At least one field (price or quantity) must be provided for update.");
        }

        @ParameterizedTest
        @ValueSource(doubles = {10.0, 20.5})
        @DisplayName("should update price when provided")
        void shouldUpdatePrice(double price) {
            Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 1);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            BookUpdate update = new BookUpdate();
            update.setPrice(BigDecimal.valueOf(price));

            Book result = service.updateBook(1L, update);

            assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(price));
            verify(repository).update(existing);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException on update")
        void shouldWrapDataAccessExceptionOnUpdate() {
            BookUpdate update = new BookUpdate();
            update.setPrice(BigDecimal.TEN);

            when(repository.findById(1L)).thenReturn(Optional.of(new Book()));
            when(repository.update(any(Book.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.updateBook(1L, update))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }

        @Test
        @DisplayName("should update quantity when provided")
        void shouldUpdatequantity() {
            Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 5);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            BookUpdate update = new BookUpdate();
            update.setQuantity(10);

            Book result = service.updateBook(1L, update);

            assertThat(result.getQuantity()).isEqualTo(10);
            verify(repository).update(existing);
        }

        @Test
        @DisplayName("should update both price and quantity when both provided")
        void shouldUpdatePriceAndQuantity() {
            Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 1);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            BookUpdate update = new BookUpdate();
            update.setPrice(BigDecimal.TEN);
            update.setQuantity(20);

            Book result = service.updateBook(1L, update);

            assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(result.getQuantity()).isEqualTo(20);
            verify(repository).update(existing);
        }
    }

    @Test
    @DisplayName("deleteBook should delegate to repository and wrap DataAccessException")
    void deleteBookBehaviour() {
        Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 1);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.deleteBook(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBook should wrap DataAccessException from repository")
    void deleteBookDataAccessException() {
        Book existing = new Book("Book 1", "Author", "Category", BigDecimal.ONE, 1);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doThrow(new DataAccessException("db error") {
        }).when(repository).deleteById(1L);

        assertThatThrownBy(() -> service.deleteBook(1L))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Database error:");
    }
}
