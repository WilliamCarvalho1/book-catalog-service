package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.ShoppingCart;

public interface GetCartUseCase {

    ShoppingCart getCart(String userId);
}