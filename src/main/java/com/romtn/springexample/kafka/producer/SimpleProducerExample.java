package com.romtn.springexample.kafka.producer;

import org.springframework.stereotype.Service;

@Service
public class SimpleProducerExample extends AbstractProducerExample {

    @Override
    protected void send0(String key, String data) {
        kafkaTemplate.send(topic, key, data).addCallback(newSendCallback());
        sendLatch.countDown();
    }

}
