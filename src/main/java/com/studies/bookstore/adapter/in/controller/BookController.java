package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookstore.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookstore.adapter.in.controller.mapper.BookUpdateWebMapper;
import com.studies.bookstore.adapter.in.controller.mapper.BookWebMapper;
import com.studies.bookstore.application.model.PagedResult;
import com.studies.bookstore.application.port.command.PartialUpdateBookCommand;
import com.studies.bookstore.application.port.command.UpdateBookCommand;
import com.studies.bookstore.application.port.in.AddBookUseCase;
import com.studies.bookstore.application.port.in.DeleteBookUseCase;
import com.studies.bookstore.application.port.in.GetBookUseCase;
import com.studies.bookstore.application.port.in.UpdateBookUseCase;
import com.studies.bookstore.domain.model.Book;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final AddBookUseCase addBookUseCase;
    private final GetBookUseCase getBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;

    public BookController(AddBookUseCase addBookUseCase, GetBookUseCase getBookUseCase,
                          UpdateBookUseCase updateBookUseCase, DeleteBookUseCase deleteBookUseCase) {
        this.addBookUseCase = addBookUseCase;
        this.getBookUseCase = getBookUseCase;
        this.updateBookUseCase = updateBookUseCase;
        this.deleteBookUseCase = deleteBookUseCase;
    }

    @PostMapping
    public ResponseEntity<EntityModel<BookResponseDTO>> addBook(@Valid @RequestBody BookRequestDTO request) {
        Book entity = BookWebMapper.toDomain(request);
        Book created = addBookUseCase.addBook(entity);
        BookResponseDTO body = BookWebMapper.toResponseDTO(created);

        EntityModel<BookResponseDTO> resource = toBookResource(created.getId(), body);

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponseDTO>> getBook(@NotNull @PathVariable Long id) {
        BookResponseDTO body = BookWebMapper.toResponseDTO(getBookUseCase.getBook(id));

        EntityModel<BookResponseDTO> resource = toBookResource(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<BookResponseDTO>>> getAllBooks(
            @RequestParam(defaultValue = "" + DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = "" + DEFAULT_SIZE) int size) {
        PagedResult<Book> pagedBooks = getBookUseCase.getAllBooks(page, size);

        List<EntityModel<BookResponseDTO>> items = pagedBooks.getContent().stream()
                .map(book -> {
                    BookResponseDTO dto = BookWebMapper.toResponseDTO(book);
                    return EntityModel.of(dto,
                            linkTo(methodOn(BookController.class).getBook(book.getId())).withSelfRel());
                })
                .toList();

        Link selfLink = linkTo(methodOn(BookController.class).getAllBooks(page, size)).withSelfRel();

        CollectionModel<EntityModel<BookResponseDTO>> collection = CollectionModel.of(items, selfLink);

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponseDTO>> updateBook(@NotNull @PathVariable Long id,
                                                                   @Valid @RequestBody BookRequestDTO request) {
        UpdateBookCommand command = BookUpdateWebMapper.toDomain(request);
        BookResponseDTO body = BookWebMapper.toResponseDTO(updateBookUseCase.updateBook(id, command));

        EntityModel<BookResponseDTO> resource = toBookResource(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponseDTO>> partialUpdateBook(@NotNull @PathVariable Long id,
                                                                          @Valid @RequestBody BookUpdateRequestDTO request) {
        PartialUpdateBookCommand command = BookUpdateWebMapper.toDomain(request);
        BookResponseDTO body = BookWebMapper.toResponseDTO(updateBookUseCase.partialUpdateBook(id, command));

        EntityModel<BookResponseDTO> resource = toBookResource(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@NotNull @PathVariable Long id) {
        deleteBookUseCase.deleteBook(id);
    }

    // Helper to consistently build HATEOAS resource for a single book
    private EntityModel<BookResponseDTO> toBookResource(Long id, BookResponseDTO body) {
        return EntityModel.of(body,
                linkTo(methodOn(BookController.class).getBook(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).getAllBooks(DEFAULT_PAGE, DEFAULT_SIZE)).withRel("books"));
    }

}
