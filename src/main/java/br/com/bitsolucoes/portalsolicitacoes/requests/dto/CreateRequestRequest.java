package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateRequestRequest(
        @Schema(description = "Título curto da solicitação.", example = "Acesso ao sistema financeiro")
        @NotBlank @Size(max = 150) String title,
        @Schema(description = "Descrição detalhada da necessidade.", example = "Solicito acesso ao módulo financeiro para consultar os relatórios mensais.")
        @NotBlank String description,
        @Schema(description = "Identificador da categoria ativa.", example = "1")
        @NotNull Long categoryId) {
}
