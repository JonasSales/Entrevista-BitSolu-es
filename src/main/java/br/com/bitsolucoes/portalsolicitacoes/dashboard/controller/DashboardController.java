package br.com.bitsolucoes.portalsolicitacoes.dashboard.controller;

import br.com.bitsolucoes.portalsolicitacoes.dashboard.dto.DashboardResponse;
import br.com.bitsolucoes.portalsolicitacoes.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Consulta os indicadores do usuário",
            description = "Retorna o resumo das solicitações do usuário autenticado por status.")
    public DashboardResponse getSummary(Authentication authentication) {
        return service.getSummary(authentication.getName());
    }
}
