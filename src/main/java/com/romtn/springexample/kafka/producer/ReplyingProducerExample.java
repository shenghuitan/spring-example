package com.romtn.springexample.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReplyingProducerExample extends AbstractProducerExample {

    ExecutorService executor = Executors.newFixedThreadPool(threads);

    @Autowired
    ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Override
    protected void send0(String key, String data) {
        executor.execute(() -> {
            replyingKafkaTemplate.send(topic, key, data).addCallback(newSendCallback());
            sendLatch.countDown();
        });
    }

    @Override
    public KafkaTemplate<String, String> getKafkaTemplate() {
        return replyingKafkaTemplate;
    }

}
