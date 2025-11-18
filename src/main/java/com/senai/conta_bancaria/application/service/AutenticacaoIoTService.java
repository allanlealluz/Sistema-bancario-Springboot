package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.gateway.MqttGateway;
import com.senai.conta_bancaria.domain.exception.AutenticacaoIoTException;
import com.senai.conta_bancaria.domain.exception.PagamentoInvalidoException;
import com.senai.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import com.senai.conta_bancaria.domain.repository.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutenticacaoIoTService {

    private final MqttGateway mqttGateway;
    private final DispositivoIoTRepository dispositivoIoTRepository;
    private final CodigoAutenticacaoRepository codigoAutenticacaoRepository;

    // Mapa para guardar "promessas" de autenticação pendentes (Key: clienteId)
    private final Map<String, CompletableFuture<String>> pendingAuthentications = new ConcurrentHashMap<>();

    private static final long TIMEOUT_SEGUNDOS = 45; // Tempo de espera pela biometria

    /**
     * Ouve o canal de entrada do MQTT (definido no MqttConfig).
     * Este método é chamado pelo Spring Integration quando uma msg chega em "banco/validacao/+".
     */
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);

        // Extrai o clienteId do tópico: banco/validacao/{clienteId}
        String[] parts = topic.split("/");
        if (parts.length != 3) {
            log.warn("Tópico MQTT mal formatado recebido: {}", topic);
            return;
        }

        try {
            String clienteId = parts[2];
            String codigoRecebido = message.getPayload(); // O código enviado pelo IoT
            log.info("Recebida resposta IoT para cliente {}: {}", clienteId, codigoRecebido);

            // Verifica se estávamos esperando uma autenticação deste cliente
            CompletableFuture<String> future = pendingAuthentications.get(clienteId);

            if (future != null && !future.isDone()) {
                // "Completa" a promessa com o código recebido
                future.complete(codigoRecebido);
            } else {
                log.warn("Recebida resposta IoT não esperada para cliente {}", clienteId);
            }
        } catch (Exception e) {
            log.error("Erro ao processar mensagem MQTT do tópico {}: {}", topic, e.getMessage());
        }
    }

    /**
     * Chamado pelo PagamentoAppService (ou Saque, Transferencia).
     * 1. Publica a solicitação no MQTT.
     * 2. Espera (com timeout) a resposta do dispositivo (que será recebida pelo handleMqttMessage).
     * 3. Valida o código recebido.
     *
     * @param clienteId O ID do cliente que deve autenticar
     * @param pagamento Opcional: pagamento a ser associado ao código
     */
    public void iniciarAutenticacaoEConfirmar(String clienteId, com.senai.conta_bancaria.domain.entity.Pagamento pagamento) {

        // 1. Verifica se o cliente tem dispositivo ativo
        dispositivoIoTRepository.findByClienteIdAndAtivo(clienteId, true)
                .orElseThrow(() -> new PagamentoInvalidoException("Cliente não possui dispositivo IoT ativo para autenticação."));

        // 2. Cria a "promessa" para esperar a resposta
        CompletableFuture<String> authFuture = new CompletableFuture<>();
        pendingAuthentications.put(clienteId, authFuture);

        // 3. Publica no tópico do cliente
        String topicoSolicitacao = "banco/autenticacao/" + clienteId;
        String payload = "AUTENTICAR_OPERACAO"; // Pode ser um JSON com valor, etc.

        log.info("Enviando solicitação de autenticação para {}", topicoSolicitacao);
        mqttGateway.sendToTopic(payload, topicoSolicitacao);

        try {
            // 4. Espera a resposta (com timeout)
            // O handleMqttMessage vai completar este 'authFuture'
            String codigoRecebido = authFuture.get(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS);

            // 5. Valida o código
            log.info("Validando código {} para cliente {}", codigoRecebido, clienteId);
            validarCodigoRecebido(clienteId, codigoRecebido, pagamento);

            log.info("Autenticação IoT para cliente {} concluída com sucesso.", clienteId);

        } catch (TimeoutException e) {
            log.warn("Timeout de autenticação IoT para cliente {}", clienteId);
            throw new AutenticacaoIoTException("Tempo limite de autenticação IoT esgotado.");
        } catch (Exception e) {
            log.error("Falha na autenticação IoT para cliente {}: {}", clienteId, e.getMessage());
            throw new AutenticacaoIoTException("Falha na validação do código IoT: " + e.getMessage());
        } finally {
            // Limpa a pendência, seja sucesso ou falha
            pendingAuthentications.remove(clienteId);
        }
    }

    /**
     * Lógica de validação do código recebido.
     * (Simplificação: apenas salva o código recebido como "validado")
     */
    private void validarCodigoRecebido(String clienteId, String codigo, com.senai.conta_bancaria.domain.entity.Pagamento pagamento) {
        // Numa implementação real, você validaria a 'chavePublica' do dispositivo
        // ou compararia com um código gerado anteriormente pelo backend.
        if (codigo == null || codigo.length() < 6) {
            throw new PagamentoInvalidoException("Código IoT inválido ou vazio.");
        }

        // Busca o Cliente (necessário para o @ManyToOne)
        // (Assumindo que ClienteRepository existe)
        // Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();

        // Salva o registro de auditoria do código
        var authCode = com.senai.conta_bancaria.domain.entity.CodigoAutenticacao.builder()
                // .cliente(cliente) // Descomente quando tiver o clienteRepository
                .codigo(codigo)
                .expiraEm(LocalDateTime.now()) // Já expirou, pois foi usado
                .validado(true)
                .pagamento(pagamento) // Associa ao pagamento
                .build();

        codigoAutenticacaoRepository.save(authCode);
    }
}
