package com.studies.adapters.output.repository;

import com.studies.adapters.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;

//public interface SpringMongoCatalogRepository extends MongoRepository<BookEntity, String> {
//}

public interface SpringCatalogRepository extends JpaRepository<BookEntity, String> {
}
