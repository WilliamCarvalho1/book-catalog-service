package com.studies.config;

import com.studies.domain.ports.output.CatalogRepositoryPort;
import com.studies.domain.service.CatalogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public CatalogService catalogService(CatalogRepositoryPort repository) {
        return new CatalogService(repository);
    }
}
