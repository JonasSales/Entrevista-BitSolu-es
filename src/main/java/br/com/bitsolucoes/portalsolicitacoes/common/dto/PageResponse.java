package br.com.bitsolucoes.portalsolicitacoes.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Contrato estável para respostas paginadas da API.
 *
 * @param <T> tipo dos itens da página
 */
@Schema(description = "Resposta paginada da API.")
public record PageResponse<T>(
        @Schema(description = "Itens da página atual.") List<T> content,
        @Schema(description = "Número da página atual, iniciando em zero.", example = "0") int number,
        @Schema(description = "Quantidade solicitada por página.", example = "10") int size,
        @Schema(description = "Quantidade de itens nesta página.", example = "3") int numberOfElements,
        @Schema(description = "Quantidade total de itens.", example = "23") long totalElements,
        @Schema(description = "Quantidade total de páginas.", example = "3") int totalPages,
        @Schema(description = "Indica se esta é a primeira página.", example = "true") boolean first,
        @Schema(description = "Indica se esta é a última página.", example = "false") boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getNumberOfElements(), page.getTotalElements(), page.getTotalPages(),
                page.isFirst(), page.isLast());
    }
}
