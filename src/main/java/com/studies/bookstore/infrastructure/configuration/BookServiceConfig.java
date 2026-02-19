package com.studies.bookstore.infrastructure.configuration;

import com.studies.bookstore.application.port.in.AddBookUseCase;
import com.studies.bookstore.application.port.in.DeleteBookUseCase;
import com.studies.bookstore.application.port.in.GetBookUseCase;
import com.studies.bookstore.application.port.in.UpdateBookUseCase;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.application.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookServiceConfig {

    @Bean
    public BookService bookService(BookRepositoryPort bookRepositoryPort) {
        return new BookService(bookRepositoryPort);
    }

    @Bean
    public AddBookUseCase addBookUseCase(BookService bookService) {
        return bookService;
    }

    @Bean
    public GetBookUseCase getBookUseCase(BookService bookService) {
        return bookService;
    }

    @Bean
    public UpdateBookUseCase updateBookUseCase(BookService bookService) {
        return bookService;
    }

    @Bean
    public DeleteBookUseCase deleteBookUseCase(BookService bookService) {
        return bookService;
    }
}