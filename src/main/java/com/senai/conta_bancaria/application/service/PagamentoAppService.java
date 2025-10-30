package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Pagamento.StatusPagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.exception.AutenticacaoIoTException;
import com.senai.conta_bancaria.domain.exception.PagamentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import com.senai.conta_bancaria.application.service.PagamentoDomainService;
import com.senai.conta_bancaria.application.dto.PagamentoDto;
import com.senai.conta_bancaria.application.dto.TaxaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoAppService {

    private final PagamentoDomainService pagamentoDomainService;
    private final AutenticacaoIoTService autenticacaoIoTService;
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;

    // Injete seu serviço de autenticação que busca o usuário logado
    // private final IAuthenticationService authenticationService;

    @Transactional
    public Pagamento realizarPagamento(PagamentoDto request, Cliente clienteLogado) {

        // 1. Buscar entidades
        // Cliente clienteLogado = authenticationService.getAuthenticatedCliente(); // Use seu serviço
        Conta conta = contaRepository.findFirstByClienteIdAndAtivo(clienteLogado.getId(), true)
                .orElseThrow(() -> new PagamentoInvalidoException("Nenhuma conta ativa encontrada para o cliente."));

        // 2. Buscar taxas (Simplificação: aplicando uma taxa padrão)
        // Numa app real, as taxas viriam por ID na request ou por regras de negócio
        Taxa tarifaPadrao = taxaRepository.findByDescricao("Tarifa Bancária")
                .orElseThrow(() -> new PagamentoInvalidoException("Taxa 'Tarifa Bancária' não configurada."));
        Set<Taxa> taxasAplicaveis = Set.of(tarifaPadrao);

        // 3. Calcular valor total (Domain Service)
        BigDecimal valorTotal = pagamentoDomainService.calcularValorTotalDebito(request.getValorPago(), taxasAplicaveis);

        // 4. Validar Saldo (Lógica da Entidade Conta)
        // (Isso lança SaldoInsuficienteException se falhar)
        conta.validarSaldoSuficiente(valorTotal);

        // 5. Criar Pagamento como PENDENTE
        Pagamento pagamento = Pagamento.builder()
                .conta(conta)
                .boleto(request.getBoleto())
                .valorPago(request.getValorPago())
                .valorTotalDebitado(valorTotal)
                .dataPagamento(LocalDateTime.now())
                .status(StatusPagamento.PENDENTE_AUTENTICACAO_IOT)
                .taxas(taxasAplicaveis)
                .build();

        Pagamento pagamentoPendente = pagamentoRepository.save(pagamento);
        log.info("Pagamento {} criado. Aguardando autenticação IoT.", pagamentoPendente.getId());

        // 6. Iniciar Autenticação IoT (Fluxo Síncrono)
        try {
            autenticacaoIoTService.iniciarAutenticacaoEConfirmar(clienteLogado.getId(), pagamentoPendente);
        } catch (AutenticacaoIoTException | PagamentoInvalidoException e) {
            // Se IoT falhar, marca pagamento como FALHA_AUTENTICACAO
            pagamentoPendente.setStatus(StatusPagamento.FALHA_AUTENTICACAO_IOT);
            pagamentoRepository.save(pagamentoPendente);
            log.warn("Falha na autenticação IoT para pagamento {}: {}", pagamentoPendente.getId(), e.getMessage());
            // Re-lança a exceção para o ControllerAdvice tratar
            throw e;
        } catch (Exception e) {
            // Outras falhas
            pagamentoPendente.setStatus(StatusPagamento.FALHA);
            pagamentoRepository.save(pagamentoPendente);
            throw new RuntimeException("Erro inesperado durante autenticação IoT.", e);
        }

        // 7. IoT OK! Efetivar o pagamento (Debitar da conta)
        try {
            log.info("Autenticação OK. Debitando valor {} da conta {}", valorTotal, conta.getId());
            conta.sacar(valorTotal); // Lógica de débito na entidade Conta
            contaRepository.save(conta);

            // 8. Atualizar Pagamento para SUCESSO
            pagamentoPendente.setStatus(StatusPagamento.SUCESSO);
            return pagamentoRepository.save(pagamentoPendente);

        } catch (SaldoInsuficienteException e) {
            // Saldo pode ter mudado entre a validação (passo 4) e o débito (passo 7)
            log.warn("Saldo tornou-se insuficiente (condição de corrida) para pagamento {}", pagamentoPendente.getId());
            pagamentoPendente.setStatus(StatusPagamento.SALDO_INSUFICIENTE);
            pagamentoRepository.save(pagamentoPendente);
            throw e;
        }
    }

    /**
     * Serviço para Gerente cadastrar nova taxa
     */
    @Transactional
    public Taxa criarTaxa(TaxaDto request) {
        if (taxaRepository.findByDescricao(request.getDescricao()).isPresent()) {
            throw new PagamentoInvalidoException("Taxa com esta descrição já existe.");
        }
        Taxa novaTaxa = Taxa.builder()
                .descricao(request.getDescricao())
                .percentual(request.getPercentual())
                .valorFixo(request.getValorFixo())
                .build();

        return taxaRepository.save(novaTaxa);
    }
}
