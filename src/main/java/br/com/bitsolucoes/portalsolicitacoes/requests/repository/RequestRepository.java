package br.com.bitsolucoes.portalsolicitacoes.requests.repository;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

    List<Request> findAllByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    long countByStatus(RequestStatus status);

    long countByRequesterId(Long requesterId);

    long countByRequesterIdAndStatus(Long requesterId, RequestStatus status);
}
