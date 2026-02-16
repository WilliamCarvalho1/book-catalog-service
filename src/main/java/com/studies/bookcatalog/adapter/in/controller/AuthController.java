package com.studies.bookcatalog.adapter.in.controller;

import com.studies.bookcatalog.adapter.in.controller.dto.AuthRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.AuthResponseDTO;
import com.studies.bookcatalog.infrastructure.configuration.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final long expirationMinutes;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.expirationMinutes = expirationMinutes;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            logger.info("Authentication attempt for user='{}'", request.username());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            String token = tokenProvider.generateToken(authentication);
            Instant expiresAt = Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES);

            logger.info("Authentication successful for user='{}'", request.username());
            return ResponseEntity.ok(new AuthResponseDTO(token, expiresAt));
        } catch (AuthenticationException ex) {
            logger.warn("Authentication failed for user='{}': {}", request.username(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // With stateless JWT, logout is handled on the client side by discarding the token.
        logger.info("Logout endpoint called - stateless JWT, token should be discarded on client side");
        return ResponseEntity.noContent().build();
    }
}
