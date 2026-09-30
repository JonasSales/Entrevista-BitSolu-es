package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChangeRequestStatusRequest(
        @Schema(description = "Novo status da solicitação.", example = "IN_PROGRESS",
                allowableValues = {"OPEN", "IN_PROGRESS", "COMPLETED"})
        @NotNull RequestStatus status) {
}
