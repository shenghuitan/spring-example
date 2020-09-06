package com.romtn.springexample.kafka.producer;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PoolProducerExample extends AbstractProducerExample {

    ExecutorService executor = Executors.newFixedThreadPool(threads);

    @Override
    protected void send0(String key, String data) {
        executor.execute(() -> {
            kafkaTemplate.send(topic, key, data).addCallback(newSendCallback());
            sendLatch.countDown();
        });
    }

}
