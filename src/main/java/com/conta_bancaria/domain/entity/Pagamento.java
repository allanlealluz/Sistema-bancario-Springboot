package com.conta_bancaria.domain.entity;

import com.conta_bancaria.domain.enums.StatusPagamento;
import com.conta_bancaria.domain.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(nullable = false)
    private String boleto;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING) // ADICIONE ESTE CAMPO
    @Column(nullable = false)
    private TipoPagamento tipoPagamento;

    @Enumerated(EnumType.STRING) // salva o enum como texto
    @Column(nullable = false)
    private StatusPagamento status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "pagamento_taxa",
            joinColumns = @JoinColumn(name = "pagamento_id"),
            inverseJoinColumns = @JoinColumn(name = "taxa_id")
    )
    private List<Taxa> taxas;
}
