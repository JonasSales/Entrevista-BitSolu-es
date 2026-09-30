package br.com.bitsolucoes.portalsolicitacoes.requests.service;

import br.com.bitsolucoes.portalsolicitacoes.common.exception.ResourceNotFoundException;
import br.com.bitsolucoes.portalsolicitacoes.requestcategories.model.RequestCategory;
import br.com.bitsolucoes.portalsolicitacoes.requestcategories.repository.RequestCategoryRepository;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.ChangeRequestStatusRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.CreateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestResponse;
import br.com.bitsolucoes.portalsolicitacoes.requests.dto.UpdateRequestRequest;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import br.com.bitsolucoes.portalsolicitacoes.requests.repository.RequestRepository;
import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import br.com.bitsolucoes.portalsolicitacoes.users.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestCategoryRepository categoryRepository;

    public RequestService(RequestRepository requestRepository, UserRepository userRepository,
                          RequestCategoryRepository categoryRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public RequestResponse create(CreateRequestRequest input, String username) {
        User requester = findUser(username);
        RequestCategory category = findCategory(input.categoryId());
        return RequestResponse.from(requestRepository.save(
                Request.create(input.title().trim(), input.description().trim(), category, requester)));
    }

    @Transactional(readOnly = true)
    public List<RequestResponse> findByRequester(String username) {
        User requester = findUser(username);
        return requestRepository.findAllByRequesterIdOrderByCreatedAtDesc(requester.getId()).stream()
                .map(RequestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RequestResponse findById(Long id, String username) {
        return RequestResponse.from(findOwnedRequest(id, username));
    }

    @Transactional
    public RequestResponse update(Long id, UpdateRequestRequest input, String username) {
        Request request = findOwnedRequest(id, username);
        ensureOpen(request);
        request.update(input.title().trim(), input.description().trim(), findCategory(input.categoryId()));
        return RequestResponse.from(request);
    }

    @Transactional
    public RequestResponse changeStatus(Long id, ChangeRequestStatusRequest input, String username) {
        Request request = findOwnedRequest(id, username);
        request.changeStatus(input.status());
        return RequestResponse.from(request);
    }

    @Transactional
    public void delete(Long id, String username) {
        Request request = findOwnedRequest(id, username);
        ensureOpen(request);
        requestRepository.delete(request);
    }

    private Request findRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada."));
    }

    private Request findOwnedRequest(Long id, String username) {
        Request request = findRequest(id);
        if (!request.getRequester().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Solicitação não encontrada.");
        }
        return request;
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));
    }

    private RequestCategory findCategory(Long id) {
        return categoryRepository.findById(id)
                .filter(RequestCategory::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada ou inativa."));
    }

    private void ensureOpen(Request request) {
        if (request.getStatus() != RequestStatus.OPEN) {
            throw new IllegalArgumentException("A solicitação só pode ser alterada enquanto estiver aberta.");
        }
    }
}
