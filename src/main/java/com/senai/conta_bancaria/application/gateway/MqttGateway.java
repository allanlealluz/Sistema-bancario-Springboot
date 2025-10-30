package com.senai.conta_bancaria.application.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

// Esta interface é a "porta de saída" da nossa aplicação para o MQTT
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    /**
     * Envia uma mensagem para um tópico MQTT específico.
     * @param payload Conteúdo da mensagem
     * @param topic Tópico de destino
     */
    void sendToTopic(String payload, @Header(MqttHeaders.TOPIC) String topic);
}
