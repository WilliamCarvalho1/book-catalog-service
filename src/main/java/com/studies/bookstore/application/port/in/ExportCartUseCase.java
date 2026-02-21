package com.studies.bookstore.application.port.in;

import com.studies.bookstore.domain.model.ShoppingCart;

public interface ExportCartUseCase {
    String exportCart(ShoppingCart cart);

    String exportCartForUser(String userId);
}