package com.senai.conta_bancaria_turma1.application.dto;

import java.math.BigDecimal;

public record TransferenciaDTO (
        String contaDestino,
        BigDecimal valor
){
}
