package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.ShoppingCart;

public interface AddItemToCartUseCase {

    ShoppingCart addItem(String userId, Long bookId, int quantity);
}