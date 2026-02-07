package com.studies.bookcatalog.adapter.in.controller;

import com.studies.bookcatalog.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.domain.model.Book;
import com.studies.bookcatalog.domain.model.BookUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CatalogControllerTest {

    private final AddBookUseCase addBookUseCase = mock(AddBookUseCase.class);
    private final GetBookUseCase getBookUseCase = mock(GetBookUseCase.class);
    private final UpdateBookUseCase updateBookUseCase = mock(UpdateBookUseCase.class);
    private final DeleteBookUseCase deleteBookUseCase = mock(DeleteBookUseCase.class);

    private final CatalogController controller = new CatalogController(
            addBookUseCase,
            getBookUseCase,
            updateBookUseCase,
            deleteBookUseCase
    );

    @Test
    @DisplayName("addBook should map request DTO to domain and return created response")
    void addBook() {
        BookRequestDTO request = new BookRequestDTO(
                "Book 1",
                "Author",
                "Category",
                BigDecimal.TEN,
                5
        );

        Book saved = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 5);

        when(addBookUseCase.addBook(any(Book.class))).thenReturn(saved);

        ResponseEntity<BookResponseDTO> response = controller.addBook(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().name()).isEqualTo("Book 1");

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(addBookUseCase).addBook(captor.capture());
        Book passed = captor.getValue();
        assertThat(passed.getName()).isEqualTo("Book 1");
        assertThat(passed.getAuthor()).isEqualTo("Author");
    }

    @Test
    @DisplayName("getBook should return mapped response DTO")
    void getBook() {
        Book book = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 5);

        when(getBookUseCase.getBook(1L)).thenReturn(book);

        ResponseEntity<BookResponseDTO> response = controller.getBook(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().name()).isEqualTo("Book 1");
    }

    @Test
    @DisplayName("getAllBooks should return list of mapped DTOs")
    void getAllBooks() {
        Book book1 = new Book(1L, "Book 1", "Author 1", "Category 1", BigDecimal.ONE, 1);
        Book book2 = new Book(2L, "Book 2", "Author 2", "Category 2", BigDecimal.TEN, 2);

        when(getBookUseCase.getAllBooks()).thenReturn(List.of(book1, book2));

        ResponseEntity<List<BookResponseDTO>> response = controller.getAllBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).id()).isEqualTo(1L);
        assertThat(response.getBody().get(1).id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("updateBook should map update DTO and return updated response")
    void updateBook() {
        BookUpdateRequestDTO request = new BookUpdateRequestDTO(
                BigDecimal.TEN,
                10
        );

        Book updated = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 10);

        when(updateBookUseCase.updateBook(eq(1L), any(BookUpdate.class))).thenReturn(updated);

        ResponseEntity<BookResponseDTO> response = controller.updateBook(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().price()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(response.getBody().quantity()).isEqualTo(10);

        ArgumentCaptor<BookUpdate> captor = ArgumentCaptor.forClass(BookUpdate.class);
        verify(updateBookUseCase).updateBook(eq(1L), captor.capture());
        BookUpdate passed = captor.getValue();
        assertThat(passed.getPrice()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(passed.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("deleteBook should delegate to use case")
    void deleteBook() {
        controller.deleteBook(1L);

        verify(deleteBookUseCase).deleteBook(1L);
    }
}
