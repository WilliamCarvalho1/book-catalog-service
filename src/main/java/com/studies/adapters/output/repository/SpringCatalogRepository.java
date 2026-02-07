package com.studies.adapters.output.repository;

import com.studies.adapters.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringCatalogRepository extends JpaRepository<BookEntity, String> {
}
