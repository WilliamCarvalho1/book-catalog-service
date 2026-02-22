package com.studies.bookstore.adapter.out.persistence.mapper;

import com.studies.bookstore.adapter.out.persistence.entity.CartItemEntity;
import com.studies.bookstore.adapter.out.persistence.entity.ShoppingCartEntity;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;

import java.util.List;

public class ShoppingCartPersistenceMapper {
    private ShoppingCartPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ShoppingCart toDomain(ShoppingCartEntity entity) {
        List<CartItem> items = entity.getItems().stream()
                .map(e -> new CartItem(
                        e.getBookId(),
                        e.getTitle(),
                        e.getUnitPrice(),
                        e.getQuantity()
                ))
                .toList();

        return new ShoppingCart(entity.getUserId(), items);
    }

    public static void updateEntityFromDomain(ShoppingCart cart, ShoppingCartEntity entity) {
        entity.getItems().clear();
        cart.getItems().forEach(domainItem -> {
            CartItemEntity itemEntity = new CartItemEntity();
            itemEntity.setCart(entity);
            itemEntity.setBookId(domainItem.getBookId());
            itemEntity.setTitle(domainItem.getTitle());
            itemEntity.setUnitPrice(domainItem.getUnitPrice());
            itemEntity.setQuantity(domainItem.getQuantity());
            entity.getItems().add(itemEntity);
        });
    }
}