package br.com.bitsolucoes.portalsolicitacoes.users.service;

import br.com.bitsolucoes.portalsolicitacoes.users.dto.AuthResponse;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final long expirationSeconds;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.expirationSeconds = expirationMinutes * 60;
    }

    public AuthResponse login(LoginRequest input) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.username(), input.password()));
        String token = jwtService.generateToken(authentication.getName());
        return new AuthResponse(token, "Bearer", expirationSeconds);
    }
}
