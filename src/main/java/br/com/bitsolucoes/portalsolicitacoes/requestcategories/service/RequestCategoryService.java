package br.com.bitsolucoes.portalsolicitacoes.requestcategories.service;

import br.com.bitsolucoes.portalsolicitacoes.requestcategories.dto.RequestCategoryResponse;
import br.com.bitsolucoes.portalsolicitacoes.requestcategories.repository.RequestCategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestCategoryService {
    private final RequestCategoryRepository repository;

    public RequestCategoryService(RequestCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RequestCategoryResponse> findActive() {
        return repository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(RequestCategoryResponse::from)
                .toList();
    }
}
