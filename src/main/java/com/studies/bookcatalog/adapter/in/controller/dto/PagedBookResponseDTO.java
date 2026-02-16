package com.studies.bookcatalog.adapter.in.controller.dto;

import java.util.List;

public record PagedBookResponseDTO(
        List<BookResponseDTO> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {
}