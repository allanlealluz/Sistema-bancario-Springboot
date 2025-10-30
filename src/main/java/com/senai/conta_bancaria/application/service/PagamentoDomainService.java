package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.domain.entity.Taxa;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PagamentoDomainService {

    /**
     * Calcula o valor total a ser debitado, somando o valor principal e as taxas.
     *
     * @param valorPrincipal Valor do boleto
     * @param taxas Conjunto de taxas a aplicar
     * @return Valor total (principal + taxas)
     */
    public BigDecimal calcularValorTotalDebito(BigDecimal valorPrincipal, Set<Taxa> taxas) {
        if (valorPrincipal == null || valorPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor principal deve ser positivo.");
        }

        BigDecimal totalTaxas = BigDecimal.ZERO;
        if (taxas != null) {
            for (Taxa taxa : taxas) {
                // Calcula taxa percentual
                BigDecimal valorTaxaPercentual = valorPrincipal.multiply(taxa.getPercentual());
                // Adiciona valor fixo
                totalTaxas = totalTaxas.add(valorTaxaPercentual).add(taxa.getValorFixo());
            }
        }

        return valorPrincipal.add(totalTaxas);
    }
}
