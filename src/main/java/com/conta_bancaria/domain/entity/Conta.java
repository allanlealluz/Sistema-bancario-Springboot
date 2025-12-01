package com.conta_bancaria.domain.entity;

import com.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.conta_bancaria.domain.exception.TransferirParaMesmaContaException;
import com.conta_bancaria.domain.exception.ValorNegativoException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "conta",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_conta_numero", columnNames = "numero"),
                @UniqueConstraint(name = "uk_cliente_tipo", columnNames = {"cliente_id", "tipo_conta"})
        })
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false, precision = 19, scale = 2) // Ajuste na precisão para valores monetários
    protected BigDecimal saldo;

    @Column(nullable = false)
    private boolean ativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", foreignKey = @ForeignKey(name = "fk_conta_cliente"))
    private Cliente cliente;

    @Column(name = "tipo_conta", insertable = false, updatable = false)
    private String tipoConta; // Necessário para a constraint única

    public abstract String getTipo();

    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "saque");
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("saque");
        }
        saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "depósito");
        saldo = saldo.add(valor);
    }


    protected static void validarValorMaiorQueZero(BigDecimal valor, String operacao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorNegativoException(operacao);
        }
    }

    public void transferir(BigDecimal valor, Conta contaDestino) {
        if (this.id.equals(contaDestino.getId())) {
            throw new TransferirParaMesmaContaException();
        }

        this.sacar(valor);
        contaDestino.depositar(valor);
    }
}


