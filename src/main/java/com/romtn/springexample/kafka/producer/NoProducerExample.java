package com.romtn.springexample.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoProducerExample extends AbstractProducerExample {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    protected void send0(String key, String data) {
        sendLatch.countDown();
        replyLatch.countDown();
    }


}
