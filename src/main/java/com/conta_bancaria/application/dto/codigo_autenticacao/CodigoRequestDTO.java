package com.conta_bancaria.application.dto.codigo_autenticacao;

import java.time.LocalDateTime;

public record CodigoRequestDTO (
    String codigo,
    LocalDateTime expiraEm,
    String clienteCpf
){}
