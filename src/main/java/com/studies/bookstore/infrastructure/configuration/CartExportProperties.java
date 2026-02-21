package com.studies.bookstore.infrastructure.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "cart.export")
public class CartExportProperties {

    /**
     * Base directory where shopping cart export JSON files will be written.
     */
    private String directory;

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}