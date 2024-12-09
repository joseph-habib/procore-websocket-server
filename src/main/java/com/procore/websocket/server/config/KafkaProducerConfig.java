package com.procore.websocket.server.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Data
@Configuration
@Validated
public class KafkaProducerConfig {

    @Value("${kafka.websocket-event.bootstrap-servers}")
    private String bootstrapServers;

    private boolean enableAuth = true;

    @Value("${kafka.websocket-event.cluster-api-key}")
    private String clusterApiKey;

    @Value("${kafka.websocket-event.cluster-api-secret}")
    private String clusterApiSecret;

    @Bean
    public ProducerFactory<String, byte[]> conversationsEventProducerFactory() {
        Map<String, Object> producerProps = new HashMap<>();

        producerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class.getName());

        producerProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.ByteArraySerializer.class.getName());

        if (enableAuth) {
            if (StringUtils.isEmpty(clusterApiKey) || StringUtils.isEmpty(clusterApiSecret)) {
                throw new IllegalArgumentException("clusterApiKey and clusterApiSecret are required");
            }

            producerProps.put(
                    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name());
            producerProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            producerProps.put(
                    SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS,
                    org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler.class.getName());
            producerProps.put(
                    SaslConfigs.SASL_JAAS_CONFIG,
                    String.format(
                            "%s required username=\"%s\" password=\"%s\"%s",
                            org.apache.kafka.common.security.plain.PlainLoginModule.class.getName(),
                            clusterApiKey,
                            clusterApiSecret,
                            ";"));
        }

        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    public KafkaTemplate<String, byte[]> conversationsEventKafkaTemplate() {
        return new KafkaTemplate<>(conversationsEventProducerFactory());
    }
}