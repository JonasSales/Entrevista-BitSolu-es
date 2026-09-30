package br.com.bitsolucoes.portalsolicitacoes.requestcategories.dto;

import br.com.bitsolucoes.portalsolicitacoes.requestcategories.model.RequestCategory;

public record RequestCategoryResponse(Long id, String code, String name) {
    public static RequestCategoryResponse from(RequestCategory category) {
        return new RequestCategoryResponse(category.getId(), category.getCode(), category.getName());
    }
}
