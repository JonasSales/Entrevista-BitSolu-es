package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Token JWT de acesso.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvLnNpbHZhIn0.assinatura")
        String accessToken,
        @Schema(description = "Tipo do token utilizado no header Authorization.", example = "Bearer")
        String tokenType,
        @Schema(description = "Tempo de expiração do token em segundos.", example = "3600")
        long expiresInSeconds) {
}
