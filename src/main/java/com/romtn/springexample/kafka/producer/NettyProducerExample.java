package com.romtn.springexample.kafka.producer;

import io.netty.channel.DefaultEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class NettyProducerExample extends AbstractProducerExample {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    DefaultEventLoopGroup group = new DefaultEventLoopGroup(threads);

    @Override
    protected void send0(String key, String data) {
        group.submit(() -> {
            kafkaTemplate.send(topic, key, data).addCallback(newSendCallback());
            sendLatch.countDown();
        });
    }

}
