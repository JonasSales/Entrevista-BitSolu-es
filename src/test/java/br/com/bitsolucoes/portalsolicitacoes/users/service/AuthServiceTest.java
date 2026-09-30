package br.com.bitsolucoes.portalsolicitacoes.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bitsolucoes.portalsolicitacoes.users.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(authenticationManager, jwtService, 60);
    }

    @Test
    void shouldAuthenticateAndReturnJwtResponse() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("joao.silva");
        when(jwtService.generateToken("joao.silva")).thenReturn("token-gerado");

        var response = service.login(new LoginRequest("joao.silva", "Senha@123"));

        assertThat(response.accessToken()).isEqualTo("token-gerado");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresInSeconds()).isEqualTo(3600L);
        verify(jwtService).generateToken("joao.silva");
    }
}
