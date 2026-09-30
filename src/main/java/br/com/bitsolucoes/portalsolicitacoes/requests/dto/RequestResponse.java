package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record RequestResponse(
        @Schema(description = "Identificador da solicitação.", example = "42") Long id,
        @Schema(description = "Título da solicitação.", example = "Acesso ao sistema financeiro") String title,
        @Schema(description = "Descrição da solicitação.", example = "Solicito acesso ao módulo financeiro.") String description,
        @Schema(description = "Identificador da categoria.", example = "1") Long categoryId,
        @Schema(description = "Nome da categoria.", example = "Tecnologia da Informação") String categoryName,
        @Schema(description = "Identificador do solicitante.", example = "1") Long requesterId,
        @Schema(description = "Nome do solicitante.", example = "João da Silva") String requesterName,
        @Schema(description = "Status atual da solicitação.", example = "OPEN") RequestStatus status,
        @Schema(description = "Data e hora de criação no formato ISO-8601.", example = "2026-09-30T12:00:00Z") Instant createdAt,
        @Schema(description = "Data e hora da última atualização no formato ISO-8601.", example = "2026-09-30T12:30:00Z") Instant updatedAt) {
    public static RequestResponse from(Request request) {
        return new RequestResponse(request.getId(), request.getTitle(), request.getDescription(),
                request.getCategory().getId(), request.getCategory().getName(),
                request.getRequester().getId(), request.getRequester().getFullName(),
                request.getStatus(), request.getCreatedAt(), request.getUpdatedAt());
    }
}
