package com.studies.bookstore.application.port.out;

import com.studies.bookstore.domain.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepositoryPort {

    Optional<ShoppingCart> findByUserId(String userId);

    ShoppingCart save(ShoppingCart cart);

    void deleteByUserId(String userId);

}