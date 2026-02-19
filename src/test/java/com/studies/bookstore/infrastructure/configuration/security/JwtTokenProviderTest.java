package com.studies.bookstore.infrastructure.configuration.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L})
    @DisplayName("generateToken should include username and validate correctly for different expirations")
    void generateAndValidateToken(long expirationMinutes) {
        String secret = "test-secret-key-for-jwt";
        JwtTokenProvider provider = new JwtTokenProvider(secret, expirationMinutes);

        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("user1");

        String token = provider.generateToken(authentication);

        assertThat(token).isNotBlank();
        assertThat(provider.validateToken(token)).isTrue();
        assertThat(provider.getUsernameFromToken(token)).isEqualTo("user1");
    }

    @Test
    @DisplayName("validateToken should return false for malformed token")
    void validateInvalidToken() {
        String secret = "test-secret-key-for-jwt";
        JwtTokenProvider provider = new JwtTokenProvider(secret, 5L);

        assertThat(provider.validateToken("not-a-jwt")).isFalse();
    }
}
