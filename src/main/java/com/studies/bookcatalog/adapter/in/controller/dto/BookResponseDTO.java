package com.studies.bookcatalog.adapter.in.controller.dto;

import java.math.BigDecimal;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String category,
        BigDecimal price,
        int publicationYear,
        int quantity
) {
}