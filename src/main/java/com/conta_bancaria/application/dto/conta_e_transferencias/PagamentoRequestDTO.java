package com.conta_bancaria.application.dto.conta_e_transferencias;

import com.conta_bancaria.domain.entity.Conta;
import com.conta_bancaria.domain.entity.Pagamento;
import com.conta_bancaria.domain.enums.StatusPagamento;
import com.conta_bancaria.domain.enums.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public record PagamentoRequestDTO(
        String contaNumero,
        String boleto,
        BigDecimal valorPago,
        StatusPagamento status,

        // escolha do tipo de pagamento do cliente
        TipoPagamento tipoPagamento,
        String codigoAutenticacao
) {
    public Pagamento toEntity(Conta conta) {

        return Pagamento.builder()
                .conta(conta)
                .boleto(this.boleto)
                .valorPago(this.valorPago)
                .dataPagamento(LocalDateTime.now())
                .status(StatusPagamento.PENDENTE)
                .tipoPagamento(this.tipoPagamento)
                .taxas(new ArrayList<>())
                .build();
    }
}