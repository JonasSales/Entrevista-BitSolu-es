package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(
        @Schema(description = "Nome de usuário para autenticação.", example = "joao.silva")
        @NotBlank @Size(max = 80) String username,
        @Schema(description = "Senha com no mínimo 8 caracteres.", example = "Senha@123", format = "password")
        @NotBlank @Size(min = 8, max = 100) String password,
        @Schema(description = "Nome completo do colaborador.", example = "João da Silva")
        @NotBlank @Size(max = 150) String fullName) {
}
