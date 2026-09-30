package br.com.bitsolucoes.portalsolicitacoes.requests.controller;

import br.com.bitsolucoes.portalsolicitacoes.requests.dto.ChangeRequestStatusRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.CreateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestFilter;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.UpdateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.service.RequestService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class RequestController {
    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RequestResponse> create(@Valid @RequestBody CreateRequestRequest input,
                                                 Authentication authentication) {
        RequestResponse response = service.create(input, authentication.getName());
        return ResponseEntity.created(URI.create("/api/v1/requests/" + response.id())).body(response);
    }

    @GetMapping
    public Page<RequestResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        Instant start = startDate == null ? null
                : startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate == null ? null
                : endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        RequestFilter filter = new RequestFilter(title, categoryId, status, start, end);
        return service.search(authentication.getName(), filter, pageable);
    }

    @GetMapping("/{id}")
    public RequestResponse findById(@PathVariable Long id, Authentication authentication) {
        return service.findById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    public RequestResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRequestRequest input,
                                  Authentication authentication) {
        return service.update(id, input, authentication.getName());
    }

    @PutMapping("/{id}/status")
    public RequestResponse changeStatus(@PathVariable Long id,
                                        @Valid @RequestBody ChangeRequestStatusRequest input,
                                        Authentication authentication) {
        return service.changeStatus(id, input, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        service.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
