package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "Nome de usuário cadastrado.", example = "joao.silva")
        @NotBlank String username,
        @Schema(description = "Senha do usuário.", example = "Senha@123")
        @NotBlank String password) {
}
