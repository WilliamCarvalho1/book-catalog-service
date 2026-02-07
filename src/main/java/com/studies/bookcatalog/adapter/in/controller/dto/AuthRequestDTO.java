package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequestDTO(
        @NotNull(message = "username must not be null")
        @NotBlank(message = "username must not be blank")
        String username,

        @NotNull(message = "password must not be null")
        @NotBlank(message = "password must not be blank")
        String password
) {
}
