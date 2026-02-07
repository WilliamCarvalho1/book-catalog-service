package com.studies.bookcatalog.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    private @NotNull(message = "username must not be null")
    @NotBlank(message = "username must not be blank")
    String username;

    private @NotNull(message = "password must not be null")
    @NotBlank(message = "password must not be blank")
    String password;

}
