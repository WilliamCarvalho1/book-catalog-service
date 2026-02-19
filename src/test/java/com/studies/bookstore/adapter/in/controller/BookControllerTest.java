package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookstore.application.model.PagedResult;
import com.studies.bookstore.application.port.command.PartialUpdateBookCommand;
import com.studies.bookstore.application.port.command.UpdateBookCommand;
import com.studies.bookstore.application.port.in.AddBookUseCase;
import com.studies.bookstore.application.port.in.DeleteBookUseCase;
import com.studies.bookstore.application.port.in.GetBookUseCase;
import com.studies.bookstore.application.port.in.UpdateBookUseCase;
import com.studies.bookstore.domain.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private final AddBookUseCase addBookUseCase = mock(AddBookUseCase.class);
    private final GetBookUseCase getBookUseCase = mock(GetBookUseCase.class);
    private final UpdateBookUseCase updateBookUseCase = mock(UpdateBookUseCase.class);
    private final DeleteBookUseCase deleteBookUseCase = mock(DeleteBookUseCase.class);

    private final BookController controller = new BookController(
            addBookUseCase,
            getBookUseCase,
            updateBookUseCase,
            deleteBookUseCase
    );

    @Test
    @DisplayName("addBook should map request DTO to domain and return created response with links")
    void addBook() {
        BookRequestDTO request = new BookRequestDTO(
                "Book 1",
                "Author",
                "Category",
                BigDecimal.TEN,
                2020,
                5
        );

        Book saved = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);

        when(addBookUseCase.addBook(any(Book.class))).thenReturn(saved);

        ResponseEntity<EntityModel<BookResponseDTO>> response = controller.addBook(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        BookResponseDTO body = response.getBody().getContent();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.title()).isEqualTo("Book 1");

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(addBookUseCase).addBook(captor.capture());
        Book passed = captor.getValue();
        assertThat(passed.getTitle()).isEqualTo("Book 1");
        assertThat(passed.getAuthor()).isEqualTo("Author");
    }

    @Test
    @DisplayName("getBook should return mapped response DTO with links")
    void getBook() {
        Book book = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);

        when(getBookUseCase.getBook(1L)).thenReturn(book);

        ResponseEntity<EntityModel<BookResponseDTO>> response = controller.getBook(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BookResponseDTO body = response.getBody().getContent();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.title()).isEqualTo("Book 1");
    }

    @Test
    @DisplayName("getAllBooks should return collection model with mapped DTOs and links")
    void getAllBooks() {
        Book book1 = new Book(1L, "Book 1", "Author 1", "Category 1", BigDecimal.ONE, 2020, 1);
        Book book2 = new Book(2L, "Book 2", "Author 2", "Category 2", BigDecimal.TEN, 2021, 2);

        PagedResult<Book> pagedResult = new PagedResult<>(List.of(book1, book2), 2L, 1, 0, 10);

        when(getBookUseCase.getAllBooks(0, 10)).thenReturn(pagedResult);

        ResponseEntity<CollectionModel<EntityModel<BookResponseDTO>>> response = controller.getAllBooks(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<EntityModel<BookResponseDTO>> content = response.getBody().getContent().stream().toList();
        assertThat(content).hasSize(2);
        assertThat(content.get(0).getContent()).isNotNull();
        assertThat(content.get(0).getContent().id()).isEqualTo(1L);
        assertThat(content.get(1).getContent()).isNotNull();
        assertThat(content.get(1).getContent().id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("getAllBooks should use default pagination parameters when none provided")
    void getAllBooksDefaultParams() {
        Book book1 = new Book(1L, "Book 1", "Author 1", "Category 1", BigDecimal.ONE, 2020, 1);
        PagedResult<Book> pagedResult = new PagedResult<>(List.of(book1), 1L, 1, 0, 10);

        when(getBookUseCase.getAllBooks(0, 10)).thenReturn(pagedResult);

        ResponseEntity<CollectionModel<EntityModel<BookResponseDTO>>> response = controller.getAllBooks(0, 10);

        verify(getBookUseCase).getAllBooks(0, 10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateBook should map update DTO and return updated response with links")
    void updateBook() {
        BookRequestDTO request = new BookRequestDTO(
                "Book 1",
                "Author",
                "Category",
                BigDecimal.TEN,
                2020,
                10
        );

        Book updated = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 10);

        when(updateBookUseCase.updateBook(eq(1L), any(UpdateBookCommand.class))).thenReturn(updated);

        ResponseEntity<EntityModel<BookResponseDTO>> response = controller.updateBook(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BookResponseDTO body = response.getBody().getContent();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.price()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(body.quantity()).isEqualTo(10);

        ArgumentCaptor<UpdateBookCommand> captor = ArgumentCaptor.forClass(UpdateBookCommand.class);
        verify(updateBookUseCase).updateBook(eq(1L), captor.capture());
        UpdateBookCommand passed = captor.getValue();
        assertThat(passed.price()).contains(BigDecimal.TEN);
        assertThat(passed.quantity()).contains(10);
    }

    @Test
    @DisplayName("deleteBook should delegate to use case")
    void deleteBook() {
        controller.deleteBook(1L);

        verify(deleteBookUseCase).deleteBook(1L);
    }

    @Test
    @DisplayName("partialUpdateBook should map request DTO to PartialUpdateBookCommand and return updated response with links")
    void partialUpdateBook() {
        BookUpdateRequestDTO request = new BookUpdateRequestDTO(
                BigDecimal.valueOf(20),
                10
        );

        Book updated = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 10);

        when(updateBookUseCase.partialUpdateBook(eq(1L), any(PartialUpdateBookCommand.class)))
                .thenReturn(updated);

        ResponseEntity<EntityModel<BookResponseDTO>> response = controller.partialUpdateBook(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BookResponseDTO body = response.getBody().getContent();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.price()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(body.quantity()).isEqualTo(10);

        ArgumentCaptor<PartialUpdateBookCommand> captor = ArgumentCaptor.forClass(PartialUpdateBookCommand.class);
        verify(updateBookUseCase).partialUpdateBook(eq(1L), captor.capture());
        PartialUpdateBookCommand passed = captor.getValue();
        assertThat(passed.price()).contains(BigDecimal.valueOf(20));
        assertThat(passed.quantity()).contains(10);
    }
}

