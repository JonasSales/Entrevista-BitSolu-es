package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateRequestRequest(
        @Schema(description = "Novo título da solicitação.", example = "Acesso ao sistema financeiro")
        @NotBlank @Size(max = 150) String title,
        @Schema(description = "Nova descrição da necessidade.", example = "Acesso necessário para o fechamento do mês.")
        @NotBlank String description,
        @Schema(description = "Identificador da categoria ativa.", example = "1")
        @NotNull Long categoryId) {
}
