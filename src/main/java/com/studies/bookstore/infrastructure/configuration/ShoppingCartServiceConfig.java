package com.studies.bookstore.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.bookstore.adapter.out.persistence.JpaShoppingCartRepository;
import com.studies.bookstore.adapter.out.persistence.ShoppingCartRepositoryAdapter;
import com.studies.bookstore.application.port.in.*;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.application.service.ShoppingCartService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingCartServiceConfig {

    @Bean
    public ShoppingCartRepositoryPort shoppingCartRepositoryPort(JpaShoppingCartRepository repository, ObjectMapper objectMapper) {
        return new ShoppingCartRepositoryAdapter(repository, objectMapper);
    }

    @Bean
    public ShoppingCartService shoppingCartService(ShoppingCartRepositoryPort cartRepository, BookRepositoryPort bookRepository) {
        return new ShoppingCartService(cartRepository, bookRepository);
    }

    @Bean
    public AddItemToCartUseCase addItemToCartUseCase(ShoppingCartService service) {
        return service;
    }

    @Bean
    public UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase(ShoppingCartService service) {
        return service;
    }

    @Bean
    public RemoveCartItemUseCase removeCartItemUseCase(ShoppingCartService service) {
        return service;
    }

    @Bean
    public GetCartUseCase getCartUseCase(ShoppingCartService service) {
        return service;
    }

    @Bean
    public ClearCartUseCase clearCartUseCase(ShoppingCartService service) {
        return service;
    }

}