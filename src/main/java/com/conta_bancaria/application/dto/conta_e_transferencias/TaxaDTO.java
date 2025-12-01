package com.conta_bancaria.application.dto.conta_e_transferencias;

import com.conta_bancaria.domain.entity.Taxa;
import com.conta_bancaria.domain.enums.TipoPagamento;

import java.math.BigDecimal;

public class TaxaDTO{
        public record TaxaRequestDTO(
                String descricao,
                BigDecimal percentual,
                BigDecimal valorFixo,
                // para definir direto no DTO o tipo do pagamento ao criar a taxa
                TipoPagamento tipoPagamento
        ){
            public static TaxaRequestDTO fromEntity(Taxa taxa){
                return new TaxaRequestDTO(
                        taxa.getDescricao(),
                        taxa.getPercentual(),
                        taxa.getValorFixo(),
                        taxa.getTipoPagamento()
                );
            }
        }
        public record TaxaResponseDTO(
                String descricao,
                BigDecimal percentual,
                BigDecimal valorFixo
        ) {
            public static TaxaResponseDTO fromEntity(Taxa taxa){
                return new TaxaResponseDTO(
                        taxa.getDescricao(),
                        taxa.getPercentual(),
                        taxa.getValorFixo()
                );
            }
        }
}
