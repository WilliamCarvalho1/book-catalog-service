package com.studies.bookcatalog.adapter.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private Instant expiresAt;

}
