package com.conta_bancaria.application.dto.codigo_autenticacao;

import com.conta_bancaria.domain.entity.CodigoAutenticacao;

import java.time.LocalDateTime;

public record CodigoResponseDTO(
        String id,
        String codigo,
        LocalDateTime expiraEm,
        boolean validado,
        String clienteCpf
){
    public static CodigoResponseDTO fromEntity(CodigoAutenticacao entity){
        return new CodigoResponseDTO(
                entity.getId(),
                entity.getCodigo(),
                entity.getExpiraEm(),
                entity.isValidado(),
                entity.getCliente().getCpf()
        );
    }
}
