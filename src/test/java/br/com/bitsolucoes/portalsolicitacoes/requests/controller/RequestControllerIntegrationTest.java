package br.com.bitsolucoes.portalsolicitacoes.requests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.bitsolucoes.portalsolicitacoes.common.exception.GlobalExceptionHandler;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.service.RequestService;
import br.com.bitsolucoes.portalsolicitacoes.users.service.CustomUserDetailsService;
import br.com.bitsolucoes.portalsolicitacoes.users.service.JwtService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RequestController.class)
@Import(GlobalExceptionHandler.class)
class RequestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "joao.silva")
    void shouldReturnPaginatedRequestsWithFilters() throws Exception {
        RequestResponse response = new RequestResponse(42L, "Acesso ao sistema financeiro",
                "Solicito acesso ao módulo financeiro.", 1L, "Tecnologia da Informação", 7L,
                "João da Silva", RequestStatus.OPEN, Instant.parse("2026-09-30T12:00:00Z"),
                Instant.parse("2026-09-30T12:00:00Z"));
        when(requestService.search(eq("joao.silva"), any(), any()))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/v1/requests")
                        .with(user("joao.silva"))
                        .param("page", "0")
                        .param("size", "5")
                        .param("status", "OPEN")
                        .param("title", "financeiro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(42))
                .andExpect(jsonPath("$.content[0].status").value("OPEN"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @WithMockUser(username = "joao.silva")
    void shouldReturnBadRequestForInvalidStatusFilter() throws Exception {
        mockMvc.perform(get("/api/v1/requests")
                        .with(user("joao.silva"))
                        .param("status", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/api/v1/requests"))
                .andExpect(jsonPath("$.fields.status").value("formato inválido"));
    }
}
