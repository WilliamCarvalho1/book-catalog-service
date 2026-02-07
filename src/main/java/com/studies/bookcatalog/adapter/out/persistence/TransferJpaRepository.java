package com.studies.bookcatalog.adapter.out.persistence;

import com.studies.bookcatalog.adapter.out.persistence.entity.JpaBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferJpaRepository extends JpaRepository<JpaBookEntity, Long> {
}
