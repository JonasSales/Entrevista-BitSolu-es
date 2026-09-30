package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import java.time.Instant;

public record RequestResponse(Long id, String title, String description, Long categoryId,
                              String categoryName, Long requesterId, String requesterName,
                              RequestStatus status, Instant createdAt, Instant updatedAt) {
    public static RequestResponse from(Request request) {
        return new RequestResponse(request.getId(), request.getTitle(), request.getDescription(),
                request.getCategory().getId(), request.getCategory().getName(),
                request.getRequester().getId(), request.getRequester().getFullName(),
                request.getStatus(), request.getCreatedAt(), request.getUpdatedAt());
    }
}
