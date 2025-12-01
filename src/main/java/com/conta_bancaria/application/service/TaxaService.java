package com.conta_bancaria.application.service;

import com.conta_bancaria.application.dto.conta_e_transferencias.TaxaDTO;
import com.conta_bancaria.domain.entity.Taxa;
import com.conta_bancaria.domain.enums.TipoPagamento;
import com.conta_bancaria.domain.repository.TaxasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxaService {
    private final TaxasRepository repository;

    @PreAuthorize("hasRole('GERENTE')")
    @Transactional
    public TaxaDTO.TaxaResponseDTO criarTaxa(TaxaDTO.TaxaRequestDTO dto) {
        Taxa taxa = Taxa.builder()
                .descricao(dto.descricao())
                .percentual(dto.percentual())
                .valorFixo(dto.valorFixo())
                .tipoPagamento(dto.tipoPagamento())
                .build();

        return TaxaDTO.TaxaResponseDTO.fromEntity(repository.save(taxa));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public List<TaxaDTO.TaxaResponseDTO> listarTaxas() {
        return repository.findAll().stream()
                .map(TaxaDTO.TaxaResponseDTO::fromEntity)
                .toList();
    }
}