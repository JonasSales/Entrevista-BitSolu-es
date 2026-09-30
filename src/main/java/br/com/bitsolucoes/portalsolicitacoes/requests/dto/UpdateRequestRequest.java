package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRequestRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank String description,
        @NotNull Long categoryId) {
}
