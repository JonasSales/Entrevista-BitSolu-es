package br.com.bitsolucoes.portalsolicitacoes.requestcategories.dto;

import br.com.bitsolucoes.portalsolicitacoes.requestcategories.model.RequestCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record RequestCategoryResponse(
        @Schema(description = "Identificador da categoria.", example = "1") Long id,
        @Schema(description = "Código único da categoria.", example = "TI") String code,
        @Schema(description = "Nome da categoria.", example = "Tecnologia da Informação") String name) {
    public static RequestCategoryResponse from(RequestCategory category) {
        return new RequestCategoryResponse(category.getId(), category.getCode(), category.getName());
    }
}
