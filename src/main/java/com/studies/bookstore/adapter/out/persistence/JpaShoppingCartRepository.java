package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.adapter.out.persistence.entity.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaShoppingCartRepository extends JpaRepository<ShoppingCartEntity, String> {
}