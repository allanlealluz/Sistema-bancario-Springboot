package com.conta_bancaria.application.service;

import com.conta_bancaria.application.dto.codigo_autenticacao.CodigoRequestDTO;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPayload;
import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.conta_bancaria.domain.repository.ClienteRepository;
import com.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IoTSubscriber {

    private final CodigoAutenticacaoRepository codigoRepo;
    private final ClienteRepository clienteRepo;

   @MqttSubscriber("banco/validacao/{clienteCpf}")
    public void receberCodigo(  @MqttPayload CodigoRequestDTO dto) {
        Cliente cliente = clienteRepo.findByCpfAndAtivoTrue(dto.clienteCpf())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));

        CodigoAutenticacao codigo = CodigoAutenticacao.builder()
                .codigo(dto.codigo())
                .expiraEm(dto.expiraEm())
                .validado(false)
                .cliente(cliente)
                .build();

        codigoRepo.save(codigo);
    }
}
