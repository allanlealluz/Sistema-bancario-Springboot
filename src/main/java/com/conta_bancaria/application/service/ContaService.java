package com.conta_bancaria.application.service;

import com.conta_bancaria.application.dto.conta_e_transferencias.ContaAtualizadaDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.ContaResumoDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.SaqueDepositoDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.TransferenciaDTO;
import com.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.conta_bancaria.domain.entity.Conta;
import com.conta_bancaria.domain.entity.ContaCorrente;
import com.conta_bancaria.domain.entity.ContaPoupanca;
import com.conta_bancaria.domain.exception.AutenticacaoIoTExpiradaException;
import com.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.conta_bancaria.domain.exception.RendimentoInvalidoException;
import com.conta_bancaria.domain.exception.TipoDeContaInvalidoException;
import com.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import com.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository repository;
    private final IoTService iotService;
    private final CodigoAutenticacaoRepository codigoRepository;

    private Conta buscarConta(String numero) {
        return repository.findByNumeroAndAtivaTrue(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta"));
    }

    @PreAuthorize("hasRole('GERENTE')")
    @Transactional(readOnly = true)
    public List<ContaResumoDTO> exibirContas() {
        return repository.findAllByAtivaTrue().stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('GERENTE')")
    @Transactional(readOnly = true)
    public ContaResumoDTO exibirContasPorNumero(String numero) {
        Conta c = buscarConta(numero);
        return ContaResumoDTO.fromEntity(c);
    }


    @PreAuthorize("hasAnyRole('GERENTE', 'CLIENTE')")
    public ContaResumoDTO atualizarConta(String numero, ContaAtualizadaDTO dto) {
        Conta c = buscarConta(numero);

        if (c instanceof ContaPoupanca contaPoupanca) {
            contaPoupanca.setRendimento(dto.rendimento());
        } else if (c instanceof ContaCorrente contaCorrente) {
            contaCorrente.setLimite(dto.limite());
            contaCorrente.setTaxa(dto.taxa());
        } else throw new TipoDeContaInvalidoException("");
        c.setSaldo(dto.saldo());

        return ContaResumoDTO.fromEntity(repository.save(c));
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'CLIENTE')")
    public void deletarConta(String numero) {
        Conta c = buscarConta(numero);
        c.setAtiva(false);
        repository.save(c);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO sacar(String numero, SaqueDepositoDTO dto) {
        Conta c = buscarConta(numero);

        iotService.solicitarAutenticacao(c.getCliente().getCpf());

        CodigoAutenticacao codigo = codigoRepository.findTopByClienteOrderByExpiraEmDesc(c.getCliente())
                .orElseThrow(() -> new AutenticacaoIoTExpiradaException("Código de autenticação não encontrado ou inválido."));

        if (codigo.getExpiraEm().isBefore(LocalDateTime.now()) || !codigo.isValidado()) {
            throw new AutenticacaoIoTExpiradaException("Autenticação falhou ou o código expirou.");
        }

        c.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(c));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO depositar(String numero, SaqueDepositoDTO dto) {
        Conta conta = buscarConta(numero);

        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO transferir(String numero, TransferenciaDTO dto) {
        Conta contaOrigem = buscarConta(numero);
        Conta contaDestino = buscarConta(dto.contaDestino());

        iotService.solicitarAutenticacao(contaOrigem.getCliente().getCpf());

        CodigoAutenticacao codigo = codigoRepository.findTopByClienteOrderByExpiraEmDesc(contaOrigem.getCliente())
                .orElseThrow(() -> new AutenticacaoIoTExpiradaException("Código de autenticação não encontrado ou inválido."));

        if (codigo.getExpiraEm().isBefore(LocalDateTime.now()) || !codigo.isValidado()) {
            throw new AutenticacaoIoTExpiradaException("Autenticação falhou ou o código expirou.");
        }

        contaOrigem.transferir(dto.valor(), contaDestino);

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ContaResumoDTO aplicarRendimento(String numero) {
        Conta conta = buscarConta(numero);
        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(poupanca));
        }
        throw new RendimentoInvalidoException();
    }
}