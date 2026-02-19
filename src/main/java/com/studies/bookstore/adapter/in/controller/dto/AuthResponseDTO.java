package com.studies.bookstore.adapter.in.controller.dto;

import java.time.Instant;

public record AuthResponseDTO(
        String token,
        Instant expiresAt
) {
}
