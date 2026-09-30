package br.com.bitsolucoes.portalsolicitacoes.requests.controller;

import br.com.bitsolucoes.portalsolicitacoes.requests.dto.ChangeRequestStatusRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.CreateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.UpdateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.service.RequestService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {
    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RequestResponse> create(@Valid @RequestBody CreateRequestRequest input) {
        RequestResponse response = service.create(input);
        return ResponseEntity.created(URI.create("/api/v1/requests/" + response.id())).body(response);
    }

    @GetMapping
    public List<RequestResponse> findByRequester(@RequestParam Long requesterId) {
        return service.findByRequester(requesterId);
    }

    @GetMapping("/{id}")
    public RequestResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RequestResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRequestRequest input) {
        return service.update(id, input);
    }

    @PutMapping("/{id}/status")
    public RequestResponse changeStatus(@PathVariable Long id,
                                        @Valid @RequestBody ChangeRequestStatusRequest input) {
        return service.changeStatus(id, input);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
