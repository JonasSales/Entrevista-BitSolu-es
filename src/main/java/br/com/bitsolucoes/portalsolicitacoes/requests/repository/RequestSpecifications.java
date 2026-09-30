package br.com.bitsolucoes.portalsolicitacoes.requests.repository;

import br.com.bitsolucoes.portalsolicitacoes.requests.dto.RequestFilter;
import br.com.bitsolucoes.portalsolicitacoes.requests.model.Request;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class RequestSpecifications {
    private RequestSpecifications() {
    }

    public static Specification<Request> withFilter(Long requesterId, RequestFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("requester").get("id"), requesterId));

            if (filter.title() != null && !filter.title().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("title")),
                        "%" + filter.title().trim().toLowerCase() + "%"));
            }
            if (filter.categoryId() != null) {
                predicates.add(builder.equal(root.get("category").get("id"), filter.categoryId()));
            }
            if (filter.status() != null) {
                predicates.add(builder.equal(root.get("status"), filter.status()));
            }
            if (filter.startDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), filter.startDate()));
            }
            if (filter.endDate() != null) {
                predicates.add(builder.lessThan(root.get("createdAt"), filter.endDate()));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
