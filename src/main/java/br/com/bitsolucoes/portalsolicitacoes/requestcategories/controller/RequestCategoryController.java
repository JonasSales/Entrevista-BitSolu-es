package br.com.bitsolucoes.portalsolicitacoes.requestcategories.controller;

import br.com.bitsolucoes.portalsolicitacoes.requestcategories.dto.RequestCategoryResponse;
import br.com.bitsolucoes.portalsolicitacoes.requestcategories.service.RequestCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/request-categories")
@SecurityRequirement(name = "bearerAuth")
public class RequestCategoryController {
    private final RequestCategoryService service;

    public RequestCategoryController(RequestCategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista categorias ativas",
            description = "Retorna as categorias disponíveis para novas solicitações.")
    public List<RequestCategoryResponse> findActive() {
        return service.findActive();
    }
}
