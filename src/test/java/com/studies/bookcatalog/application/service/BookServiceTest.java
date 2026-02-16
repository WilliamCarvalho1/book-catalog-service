package com.studies.bookcatalog.application.service;

import com.studies.bookcatalog.application.exception.InvalidRequestException;
import com.studies.bookcatalog.application.exception.RequestNotFoundException;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.domain.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
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
        Book request = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);
        Book saved = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);

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
            Book book = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);

            when(repository.findById(1L)).thenReturn(Optional.of(book));

            Book result = service.getBook(1L);

            assertThat(result).isSameAs(book);
        }
    }

    @Nested
    @DisplayName("updateBook")
    class UpdateBook {

        @ParameterizedTest
        @ValueSource(doubles = {10.0, 20.5})
        @DisplayName("should update price when provided")
        void shouldUpdatePrice(double price) {
            Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 1);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            UpdateBookCommand updateBookCommand = new UpdateBookCommand(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    java.util.Optional.of(BigDecimal.valueOf(price)),
                    Optional.empty(),
                    Optional.empty()
            );

            Book result = service.updateBook(1L, updateBookCommand);

            assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(price));
            verify(repository).update(existing);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException on update")
        void shouldWrapDataAccessExceptionOnUpdate() {
            UpdateBookCommand updateBookCommand = new UpdateBookCommand(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    java.util.Optional.of(BigDecimal.TEN),
                    Optional.empty(),
                    Optional.empty()
            );

            Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 1);
            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.updateBook(1L, updateBookCommand))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }

        @Test
        @DisplayName("should update quantity when provided")
        void shouldUpdateQuantity() {
            Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 5);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            UpdateBookCommand updateBookCommand = new UpdateBookCommand(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    java.util.Optional.of(10)
            );

            Book result = service.updateBook(1L, updateBookCommand);

            assertThat(result.getQuantity()).isEqualTo(10);
            verify(repository).update(existing);
        }

        @Test
        @DisplayName("should update both price and quantity when both provided")
        void shouldUpdatePriceAndQuantity() {
            Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 1);

            when(repository.findById(1L)).thenReturn(Optional.of(existing));
            when(repository.update(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            UpdateBookCommand updateBookCommand = new UpdateBookCommand(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    java.util.Optional.of(BigDecimal.TEN),
                    Optional.empty(),
                    java.util.Optional.of(20)
            );

            Book result = service.updateBook(1L, updateBookCommand);

            assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(result.getQuantity()).isEqualTo(20);
            verify(repository).update(existing);
        }
    }

    @Test
    @DisplayName("deleteBook should delegate to repository and wrap DataAccessException")
    void deleteBookBehaviour() {
        Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 1);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.deleteBook(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBook should wrap DataAccessException from repository")
    void deleteBookDataAccessException() {
        Book existing = new Book(1L, "Book 1", "Author", "Category", BigDecimal.ONE, 2020, 1);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doThrow(new DataAccessException("db error") {
        }).when(repository).deleteById(1L);

        assertThatThrownBy(() -> service.deleteBook(1L))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Database error:");
    }

}
