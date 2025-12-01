package com.conta_bancaria.application.dto.conta_e_transferencias;

import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.Conta;
import com.conta_bancaria.domain.entity.ContaCorrente;
import com.conta_bancaria.domain.entity.ContaPoupanca;
import com.conta_bancaria.domain.exception.TipoDeContaInvalidoException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaResumoDTO(
        @NotBlank(message = "O número da conta é obrigatório")
        @Pattern(regexp = "\\d{5,20}", message = "Número da conta deve conter entre 5 e 20 dígitos")
        String numero,

        @NotBlank(message = "O tipo da conta é obrigatório")
        @Pattern(regexp = "CORRENTE|POUPANCA", message = "Tipo deve ser CORRENTE ou POUPANCA")
        String tipo,

        @NotNull @PositiveOrZero(message = "O saldo não pode ser negativo")
        BigDecimal saldo
) {

    public Conta toEntity(Cliente cliente) {
        if ("CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .cliente(cliente)
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .taxa(new BigDecimal("0.05"))
                    .limite(new BigDecimal("500.00"))
                    .ativa(true)
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .cliente(cliente)
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .rendimento(new BigDecimal("0.01"))
                    .ativa(true)
                    .build();
        }
        throw new TipoDeContaInvalidoException(tipo);
    }

    public static ContaResumoDTO fromEntity(Conta conta) {
        return new ContaResumoDTO(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}
