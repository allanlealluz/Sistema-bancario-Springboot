package com.conta_bancaria.application.service;

import com.conta_bancaria.application.dto.codigo_autenticacao.CodigoRequestDTO;
import com.conta_bancaria.application.dto.codigo_autenticacao.CodigoResponseDTO;
import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.conta_bancaria.domain.exception.AutenticacaoIoTExpiradaException;
import com.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.conta_bancaria.domain.repository.ClienteRepository;
import com.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CodigoAutenticacaoService {
    private final CodigoAutenticacaoRepository repository;
    private final ClienteRepository clienteRepository;

    //registra o novo código que é enviado pelo IoT
    public CodigoResponseDTO registrarCodigo(CodigoRequestDTO dto){
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(dto.clienteCpf())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));

        // criando nova instancia da entidade CodigoAutenticacao
        CodigoAutenticacao codigo = CodigoAutenticacao.builder()
                .codigo(dto.codigo())
                .expiraEm(dto.expiraEm())
                .validado(false)
                .cliente(cliente)
                .build();

        return CodigoResponseDTO.fromEntity(repository.save(codigo));
    }

    //validação do código recebido via IoT
    public CodigoResponseDTO validarCodigo(String codigo, String clienteCpf){
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(clienteCpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));

        CodigoAutenticacao codigoAutenticacao = repository.findTopByClienteOrderByExpiraEmDesc(cliente)
                .orElseThrow(() -> new AutenticacaoIoTExpiradaException("Código não encontrado ou inválido."));

        //validações para verificar se o código está expirado ou se o código é inválido (respectivamente)
        if (codigoAutenticacao.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new AutenticacaoIoTExpiradaException("Autenticação falhou ou o código expirou." );
        }

        if (!codigoAutenticacao.getCodigo().equals(codigo)){
            throw new AutenticacaoIoTExpiradaException("Código não encontrado ou inválido.");
        }

        codigoAutenticacao.setValidado(true);
        repository.save(codigoAutenticacao);
        return CodigoResponseDTO.fromEntity(codigoAutenticacao);
    }
}
