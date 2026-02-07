package com.studies.bookcatalog.adapter.in.controller;

import com.studies.bookcatalog.adapter.in.controller.dto.AuthRequestDTO;
import com.studies.bookcatalog.adapter.in.controller.dto.AuthResponseDTO;
import com.studies.bookcatalog.infrastructure.configuration.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
    private final long expirationMinutes = 5L;

    private final AuthController controller = new AuthController(authenticationManager, tokenProvider, expirationMinutes);

    @Test
    @DisplayName("login should return token when authentication succeeds")
    void loginSuccess() {
        AuthRequestDTO request = new AuthRequestDTO("user", "password");

        Authentication authResult = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authResult);
        when(tokenProvider.generateToken(authResult)).thenReturn("jwt-token");

        ResponseEntity<AuthResponseDTO> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isEqualTo("jwt-token");

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication passedAuth = captor.getValue();
        assertThat(passedAuth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
    }

    @Test
    @DisplayName("login should return 401 when authentication fails")
    void loginFailure() {
        com.studies.bookcatalog.adapter.in.controller.dto.AuthRequestDTO request =
                new com.studies.bookcatalog.adapter.in.controller.dto.AuthRequestDTO("user", "wrong");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(mock(AuthenticationException.class));

        ResponseEntity<AuthResponseDTO> response = controller.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("logout should return 204")
    void logout() {
        ResponseEntity<Void> response = controller.logout();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
