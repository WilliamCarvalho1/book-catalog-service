package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.ShoppingCart;

public interface RemoveCartItemUseCase {

    ShoppingCart removeItem(String userId, Long bookId);
}