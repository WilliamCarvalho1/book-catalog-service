package com.studies.bookstore.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.bookstore.adapter.out.persistence.entity.ShoppingCartEntity;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

public class ShoppingCartRepositoryAdapter implements ShoppingCartRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartRepositoryAdapter.class);

    private final JpaShoppingCartRepository repository;
    private final ObjectMapper objectMapper;

    public ShoppingCartRepositoryAdapter(JpaShoppingCartRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ShoppingCart> findByUserId(String userId) {
        return repository.findById(userId)
                .map(entity -> {
                    try {
                        return objectMapper.readValue(entity.getCartJson(), ShoppingCart.class);
                    } catch (JsonProcessingException e) {
                        logger.error("Error deserializing cart JSON for user '{}': {}", userId, e.getMessage(), e);
                        return null;
                    }
                });
    }

    @Override
    public ShoppingCart save(ShoppingCart cart) throws DataAccessException {
        try {
            String json = objectMapper.writeValueAsString(cart);
            ShoppingCartEntity entity = new ShoppingCartEntity();
            entity.setUserId(cart.getUserId());
            entity.setCartJson(json);
            repository.save(entity);
            return cart;
        } catch (JsonProcessingException e) {
            logger.error("Error serializing cart JSON for user '{}': {}", cart.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Error serializing cart JSON", e);
        }
    }

    @Override
    public void deleteByUserId(String userId) throws DataAccessException {
        repository.deleteById(userId);
    }
}