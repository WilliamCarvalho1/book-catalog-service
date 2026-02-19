package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.adapter.out.persistence.entity.JpaBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookRepository extends JpaRepository<JpaBookEntity, Long> {
}
