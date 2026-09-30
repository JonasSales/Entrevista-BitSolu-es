package br.com.bitsolucoes.portalsolicitacoes.users.controller;

import br.com.bitsolucoes.portalsolicitacoes.users.dto.AuthResponse;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.LoginRequest;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.RegisterRequest;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.UserResponse;
import br.com.bitsolucoes.portalsolicitacoes.users.service.AuthService;
import br.com.bitsolucoes.portalsolicitacoes.users.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest input) {
        UserResponse response = userService.register(input);
        return ResponseEntity.created(URI.create("/api/v1/users/" + response.id())).body(response);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest input) {
        return authService.login(input);
    }
}
