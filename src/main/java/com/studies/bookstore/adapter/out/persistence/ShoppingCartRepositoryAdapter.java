package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.adapter.out.persistence.entity.ShoppingCartEntity;
import com.studies.bookstore.adapter.out.persistence.mapper.ShoppingCartPersistenceMapper;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

public class ShoppingCartRepositoryAdapter implements ShoppingCartRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartRepositoryAdapter.class);

    private final JpaShoppingCartRepository repository;

    public ShoppingCartRepositoryAdapter(JpaShoppingCartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ShoppingCart> findByUserId(String userId) {
        return repository.findById(userId)
                .map(ShoppingCartPersistenceMapper::toDomain);
    }

    @Override
    public ShoppingCart save(ShoppingCart cart) throws DataAccessException {
        ShoppingCartEntity entity = repository.findById(cart.getUserId()).orElseGet(() -> {
            ShoppingCartEntity e = new ShoppingCartEntity();
            e.setUserId(cart.getUserId());
            return e;
        });

        ShoppingCartPersistenceMapper.updateEntityFromDomain(cart, entity);
        ShoppingCartEntity saved = repository.save(entity);
        return ShoppingCartPersistenceMapper.toDomain(saved);
    }

    @Override
    public void deleteByUserId(String userId) throws DataAccessException {
        repository.deleteById(userId);
    }

}