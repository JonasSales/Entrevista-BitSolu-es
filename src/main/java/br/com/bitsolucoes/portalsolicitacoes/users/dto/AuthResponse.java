package br.com.bitsolucoes.portalsolicitacoes.users.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
