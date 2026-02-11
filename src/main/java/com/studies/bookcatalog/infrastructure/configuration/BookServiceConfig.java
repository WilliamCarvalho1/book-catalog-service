package com.studies.bookcatalog.infrastructure.configuration;

import com.studies.bookcatalog.application.port.in.AddBookUseCase;
import com.studies.bookcatalog.application.port.in.DeleteBookUseCase;
import com.studies.bookcatalog.application.port.in.GetBookUseCase;
import com.studies.bookcatalog.application.port.in.UpdateBookUseCase;
import com.studies.bookcatalog.application.port.out.BookRepositoryPort;
import com.studies.bookcatalog.application.service.BookService;
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