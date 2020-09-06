package com.romtn.springexample.kafka.producer;

import org.springframework.stereotype.Service;

@Service
public class NoProducerExample extends AbstractProducerExample {

    @Override
    protected void send0(String key, String data) {
        sendLatch.countDown();
        replyLatch.countDown();
    }

}
