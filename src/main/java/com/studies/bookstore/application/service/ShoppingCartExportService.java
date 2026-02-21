package com.studies.bookstore.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.bookstore.application.port.in.ExportCartUseCase;
import com.studies.bookstore.application.port.in.GetCartUseCase;
import com.studies.bookstore.domain.model.ShoppingCart;
import com.studies.bookstore.infrastructure.configuration.CartExportProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ShoppingCartExportService implements ExportCartUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartExportService.class);

    private final ObjectMapper objectMapper;
    private final CartExportProperties properties;
    private final GetCartUseCase getCartUseCase;

    public ShoppingCartExportService(ObjectMapper objectMapper,
                                     CartExportProperties properties,
                                     GetCartUseCase getCartUseCase) {
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.getCartUseCase = getCartUseCase;
    }

    @Override
    public String exportCartForUser(String userId) {
        ShoppingCart cart = getCartUseCase.getCart(userId);
        return exportCart(cart);
    }

    /**
     * Exports the given shopping cart to a JSON file in the configured export directory.
     * The file name is derived from the user's id.
     *
     * @param cart the shopping cart to export
     * @return the absolute path of the created/updated JSON file
     */
    @Override
    public String exportCart(ShoppingCart cart) {
        String baseDir = properties.getDirectory();
        if (baseDir == null || baseDir.isBlank()) {
            throw new IllegalStateException("Cart export directory is not configured");
        }

        try {
            String fileName = String.format("shopping-cart-%s.json", sanitizeFileName(cart.getUserId()));
            Path directoryPath = Paths.get(baseDir).toAbsolutePath();
            Files.createDirectories(directoryPath);

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cart);
            Path filePath = directoryPath.resolve(fileName);
            Files.writeString(filePath, json, StandardCharsets.UTF_8);

            logger.info("Exported shopping cart for user '{}' to file '{}'", cart.getUserId(), filePath);
            return filePath.toString();
        } catch (JsonProcessingException e) {
            logger.error("Error serializing shopping cart for user '{}': {}", cart.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Error serializing shopping cart to JSON", e);
        } catch (IOException e) {
            logger.error("Error writing shopping cart JSON file for user '{}': {}", cart.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Error writing shopping cart JSON file", e);
        }
    }

    private String sanitizeFileName(String userId) {
        // Replace characters that are problematic in file names on common OSes
        return userId.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
