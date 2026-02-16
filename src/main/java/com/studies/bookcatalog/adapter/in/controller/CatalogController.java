package com.studies.bookcatalog.adapter.in.controller;

import com.studies.bookcatalog.adapter.in.controller.dto.BookRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.BookResponseDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.BookUpdateRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.PagedBookResponseDTO;
import com.studies.bookcatalog.adapter.in.controller.mapper.BookUpdateWebMapper;
import com.studies.bookcatalog.adapter.in.controller.mapper.BookWebMapper;
import com.studies.bookcatalog.application.model.PagedResult;
import com.studies.bookcatalog.application.port.command.UpdateBookCommand;
import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.domain.model.Book;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = "bearerAuth")
public class CatalogController {

    private final AddBookUseCase addBookUseCase;
    private final GetBookUseCase getBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;

    public CatalogController(AddBookUseCase addBookUseCase, GetBookUseCase getBookUseCase,
                             UpdateBookUseCase updateBookUseCase, DeleteBookUseCase deleteBookUseCase) {
        this.addBookUseCase = addBookUseCase;
        this.getBookUseCase = getBookUseCase;
        this.updateBookUseCase = updateBookUseCase;
        this.deleteBookUseCase = deleteBookUseCase;
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO request) {
        Book entity = BookWebMapper.toDomain(request);
        Book created = addBookUseCase.addBook(entity);
        BookResponseDTO response = BookWebMapper.toResponseDTO(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBook(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(BookWebMapper.toResponseDTO(getBookUseCase.getBook(id)));
    }

    @GetMapping("")
    public ResponseEntity<PagedBookResponseDTO> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResult<Book> pagedBooks = getBookUseCase.getAllBooks(page, size);

        List<BookResponseDTO> content = BookWebMapper.toResponseDTOList(pagedBooks.getContent());
        PagedBookResponseDTO response = new PagedBookResponseDTO(
                content,
                pagedBooks.getTotalElements(),
                pagedBooks.getTotalPages(),
                pagedBooks.getPageNumber(),
                pagedBooks.getPageSize()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@NotNull @PathVariable Long id,
                                                      @Valid @RequestBody BookUpdateRequestDTO request) {
        UpdateBookCommand command = BookUpdateWebMapper.toDomain(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BookWebMapper.toResponseDTO(updateBookUseCase.updateBook(id, command)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@NotNull @PathVariable Long id) {
        deleteBookUseCase.deleteBook(id);
    }
}
