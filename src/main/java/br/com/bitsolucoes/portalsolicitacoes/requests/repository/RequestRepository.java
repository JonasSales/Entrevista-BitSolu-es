package br.com.bitsolucoes.portalsolicitacoes.requests.repository;

import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.RequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    long countByStatus(RequestStatus status);
}
