package com.conta_bancaria.application.dto.conta_e_transferencias;

import com.conta_bancaria.domain.entity.Pagamento;
import com.conta_bancaria.domain.enums.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDTO(
        String contaNumero,
        String boleto,
        BigDecimal valorPago,
        LocalDateTime dataPagamento,
        StatusPagamento status,
        List<TaxaDTO.TaxaResponseDTO> taxas
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<TaxaDTO.TaxaResponseDTO> taxas = pagamento.getTaxas().stream().map(TaxaDTO.TaxaResponseDTO::fromEntity).toList();

        return new PagamentoResponseDTO(
                pagamento.getConta().getNumero(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                taxas
        );
    }
}
