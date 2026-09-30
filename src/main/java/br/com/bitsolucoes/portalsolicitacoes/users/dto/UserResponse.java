package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "Identificador do usuário.", example = "1") Long id,
        @Schema(description = "Nome de usuário.", example = "joao.silva") String username,
        @Schema(description = "Nome completo do usuário.", example = "João da Silva") String fullName) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getFullName());
    }
}
