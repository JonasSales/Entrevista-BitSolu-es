package br.com.bitsolucoes.portalsolicitacoes.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DashboardResponse(
        @Schema(description = "Quantidade total de solicitações do usuário.", example = "12")
        long total,
        @Schema(description = "Quantidade de solicitações abertas.", example = "5")
        long open,
        @Schema(description = "Quantidade de solicitações em atendimento.", example = "4")
        long inProgress,
        @Schema(description = "Quantidade de solicitações concluídas.", example = "3")
        long completed) {
}
