package br.com.bitsolucoes.portalsolicitacoes.dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bitsolucoes.portalsolicitacoes.common.exception.ResourceNotFoundException;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.repository.RequestRepository;
import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import br.com.bitsolucoes.portalsolicitacoes.users.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private User user;

    @InjectMocks
    private DashboardService service;

    @Test
    void shouldReturnCountersForAuthenticatedUser() {
        when(userRepository.findByUsername("joao.silva")).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(7L);
        when(requestRepository.countByRequesterId(7L)).thenReturn(12L);
        when(requestRepository.countByRequesterIdAndStatus(7L, RequestStatus.OPEN)).thenReturn(5L);
        when(requestRepository.countByRequesterIdAndStatus(7L, RequestStatus.IN_PROGRESS)).thenReturn(4L);
        when(requestRepository.countByRequesterIdAndStatus(7L, RequestStatus.COMPLETED)).thenReturn(3L);

        var response = service.getSummary("joao.silva");

        assertThat(response.total()).isEqualTo(12L);
        assertThat(response.open()).isEqualTo(5L);
        assertThat(response.inProgress()).isEqualTo(4L);
        assertThat(response.completed()).isEqualTo(3L);
        verify(requestRepository).countByRequesterId(7L);
    }

    @Test
    void shouldRejectUnknownAuthenticatedUser() {
        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSummary("inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário autenticado não encontrado.");
    }
}
