package br.com.bitsolucoes.portalsolicitacoes.requests.controller;

import br.com.bitsolucoes.portalsolicitacoes.requests.dto.ChangeRequestStatusRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.CreateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestFilter;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.UpdateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/requests")
@SecurityRequirement(name = "bearerAuth")
public class RequestController {
    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria uma solicitação",
            description = "Registra uma nova solicitação vinculada ao usuário autenticado.")
    public ResponseEntity<RequestResponse> create(@Valid @RequestBody CreateRequestRequest input,
                                                 Authentication authentication) {
        RequestResponse response = service.create(input, authentication.getName());
        return ResponseEntity.created(URI.create("/api/v1/requests/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista solicitações paginadas",
            description = "Consulta somente as solicitações do usuário autenticado, com filtros opcionais.")
    public Page<RequestResponse> search(
            @Parameter(description = "Filtra pelo título, ignorando maiúsculas e minúsculas.", example = "financeiro")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filtra pelo identificador da categoria.", example = "1")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Filtra pelo status da solicitação.", example = "OPEN",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(allowableValues = {"OPEN", "IN_PROGRESS", "COMPLETED"}))
            @RequestParam(required = false) RequestStatus status,
            @Parameter(description = "Data inicial da criação, no formato yyyy-MM-dd.", example = "2026-09-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Data final da criação, no formato yyyy-MM-dd.", example = "2026-09-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication authentication) {
        Instant start = startDate == null ? null
                : startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate == null ? null
                : endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        RequestFilter filter = new RequestFilter(title, categoryId, status, start, end);
        return service.search(authentication.getName(), filter, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma solicitação",
            description = "Consulta uma solicitação pertencente ao usuário autenticado.")
    public RequestResponse findById(
            @Parameter(description = "Identificador da solicitação.", example = "42")
            @PathVariable Long id, Authentication authentication) {
        return service.findById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma solicitação aberta")
    public RequestResponse update(
                                  @Parameter(description = "Identificador da solicitação.", example = "42")
                                  @PathVariable Long id, @Valid @RequestBody UpdateRequestRequest input,
                                  Authentication authentication) {
        return service.update(id, input, authentication.getName());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Altera o status de uma solicitação")
    public RequestResponse changeStatus(
                                        @Parameter(description = "Identificador da solicitação.", example = "42")
                                        @PathVariable Long id,
                                        @Valid @RequestBody ChangeRequestStatusRequest input,
                                        Authentication authentication) {
        return service.changeStatus(id, input, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma solicitação aberta")
    public ResponseEntity<Void> delete(
                                       @Parameter(description = "Identificador da solicitação.", example = "42")
                                       @PathVariable Long id, Authentication authentication) {
        service.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
