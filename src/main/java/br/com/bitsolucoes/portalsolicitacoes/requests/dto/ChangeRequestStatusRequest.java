package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeRequestStatusRequest(@NotNull RequestStatus status) {
}
