package com.example.conta_bancaria.application.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;
import com.example.conta_bancaria.domain.entity.ContaCorrente;
import com.example.conta_bancaria.domain.entity.ContaPoupanca;

public record ContaDTO(
        String tipo,
        String numero,
        Double saldo,
        Double limiteChequeEspecial, // só para ContaCorrente
        Double taxaRendimento       // só para ContaPoupanca
) {

    public static ContaDTO fromEntity(Conta conta) {
        if (conta instanceof ContaCorrente cc) {
            return new ContaDTO("ContaCorrente", cc.getNumero(), cc.getSaldo(),
                    cc.getLimiteChequeEspecial(), null);
        } else if (conta instanceof ContaPoupanca cp) {
            return new ContaDTO("ContaPoupanca", cp.getNumero(), cp.getSaldo(),
                    null, cp.getTaxaRendimento());
        }
        return null;
    }

    public Conta toEntity() {
        if ("ContaCorrente".equals(tipo)) {
            ContaCorrente cc = new ContaCorrente();
            cc.setNumero(numero);
            cc.setSaldo(saldo);
            cc.setLimiteChequeEspecial(limiteChequeEspecial != null ? limiteChequeEspecial : 0.0);
            return cc;
        } else if ("ContaPoupanca".equals(tipo)) {
            ContaPoupanca cp = new ContaPoupanca();
            cp.setNumero(numero);
            cp.setSaldo(saldo);
            cp.setTaxaRendimento(taxaRendimento != null ? taxaRendimento : 0.0);
            return cp;
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido: " + tipo);
        }
    }
}
