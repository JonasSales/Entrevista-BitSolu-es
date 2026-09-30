package br.com.bitsolucoes.portalsolicitacoes.requestcategories.repository;

import br.com.bitsolucoes.portalsolicitacoes.requestcategories.model.RequestCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestCategoryRepository extends JpaRepository<RequestCategory, Long> {

    Optional<RequestCategory> findByCodeAndActiveTrue(String code);

    List<RequestCategory> findAllByActiveTrueOrderByNameAsc();
}
