package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 80) String username,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 150) String fullName) {
}
