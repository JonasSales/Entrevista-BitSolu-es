package br.com.bitsolucoes.portalsolicitacoes.common.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp, int status, String error, String message,
                       String method, String path, Map<String, String> fields) {
}
