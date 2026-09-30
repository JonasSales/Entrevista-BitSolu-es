package br.com.bitsolucoes.portalsolicitacoes.users.dto;

import br.com.bitsolucoes.portalsolicitacoes.users.model.User;

public record UserResponse(Long id, String username, String fullName) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getFullName());
    }
}
