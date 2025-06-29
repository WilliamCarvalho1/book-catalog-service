package com.studies.adapters.input.rest;

import com.studies.domain.dto.BookDTO;
import com.studies.domain.ports.input.AddBookUseCase;
import com.studies.domain.ports.input.DeleteBookUseCase;
import com.studies.domain.ports.input.GetBookUseCase;
import com.studies.domain.ports.input.UpdateBookUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
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
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO addBook(@RequestBody BookDTO bookDTO) {
        return addBookUseCase.addBook(bookDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO getBook(@PathVariable String id) {
        return getBookUseCase.getBook(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDTO> getAllBooks() {
        return getBookUseCase.getAllBooks();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        return updateBookUseCase.updateBook(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        deleteBookUseCase.deleteBook(id);
    }

}