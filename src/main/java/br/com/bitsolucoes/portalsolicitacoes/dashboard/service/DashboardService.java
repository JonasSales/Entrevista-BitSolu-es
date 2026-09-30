package br.com.bitsolucoes.portalsolicitacoes.dashboard.service;

import br.com.bitsolucoes.portalsolicitacoes.common.exception.ResourceNotFoundException;
import br.com.bitsolucoes.portalsolicitacoes.dashboard.dto.DashboardResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.repository.RequestRepository;
import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import br.com.bitsolucoes.portalsolicitacoes.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public DashboardService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getSummary(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));
        Long userId = user.getId();

        return new DashboardResponse(
                requestRepository.countByRequesterId(userId),
                requestRepository.countByRequesterIdAndStatus(userId, RequestStatus.OPEN),
                requestRepository.countByRequesterIdAndStatus(userId, RequestStatus.IN_PROGRESS),
                requestRepository.countByRequesterIdAndStatus(userId, RequestStatus.COMPLETED));
    }
}
