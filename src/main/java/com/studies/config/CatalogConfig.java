package com.studies.config;

import com.studies.adapters.output.mongo_repository.MongoCatalogRepository;
import com.studies.adapters.output.mongo_repository.SpringMongoCatalogRepository;
import com.studies.domain.ports.output.CatalogRepositoryPort;
import com.studies.domain.service.CatalogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public CatalogRepositoryPort catalogRepository(SpringMongoCatalogRepository mongoRepository) {
        return new MongoCatalogRepository(mongoRepository);
    }

    @Bean
    public CatalogService catalogService(CatalogRepositoryPort repository) {
        return new CatalogService(repository);
    }
}
