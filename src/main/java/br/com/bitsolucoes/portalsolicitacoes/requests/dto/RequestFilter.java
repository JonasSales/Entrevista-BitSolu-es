package br.com.bitsolucoes.portalsolicitacoes.requests.dto;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import java.time.Instant;

public record RequestFilter(String title, Long categoryId, RequestStatus status,
                            Instant startDate, Instant endDate) {
}
