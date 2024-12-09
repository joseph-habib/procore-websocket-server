package com.procore.websocket.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, byte[]> conversationsEventKafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, byte[]> conversationsEventKafkaTemplate) {
        this.conversationsEventKafkaTemplate = conversationsEventKafkaTemplate;
    }

    @Value("${kafka.websocket-event.topics.websocket-topic}${kafka.websocket-event.dlq-suffix}")
    private String dlqTopic;

    @Value("${kafka.websocket-event.topics.websocket-topic}")
    private String topic;

    public void sendToDlq(byte[] eventRecord, String key) {

        CompletableFuture<SendResult<String, byte[]>> future =
                conversationsEventKafkaTemplate.send(dlqTopic, key, eventRecord);
        future.whenComplete(
                (result, ex) -> {
                    if (ex != null) {
                        logger.error("Unable to send record to DLQ", ex);
                    } else {
                        logger.info(
                                "Record sent to DLQ. Topic={} Offset={} Partition={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().offset(),
                                result.getRecordMetadata().partition());
                    }
                });
    }

    public void send(byte[] eventRecord, String key) {

        CompletableFuture<SendResult<String, byte[]>> future =
                conversationsEventKafkaTemplate.send(topic, key, eventRecord);
        future.whenComplete(
                (result, ex) -> {
                    if (ex != null) {
                        logger.error("Unable to send record", ex);
                    } else {
                        logger.info(
                                "Record sent. Topic={} Offset={} Partition={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().offset(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}
