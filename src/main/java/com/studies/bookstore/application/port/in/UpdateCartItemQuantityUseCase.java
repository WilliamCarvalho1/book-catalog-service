package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.ShoppingCart;

public interface UpdateCartItemQuantityUseCase {

    ShoppingCart updateItemQuantity(String userId, Long bookId, int quantity);
}