package com.procore.websocket.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaListenerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

    private final KafkaProducerService kafkaProducerService;

    public KafkaListenerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }


}
