package com.romtn.springexample.kafka.producer;

import io.netty.channel.DefaultEventLoopGroup;
import org.springframework.stereotype.Service;

@Service
public class NettyProducerExample extends AbstractProducerExample {

    DefaultEventLoopGroup group = new DefaultEventLoopGroup(threads);

    @Override
    protected void send0(String key, String data) {
        group.submit(() -> {
            kafkaTemplate.send(topic, key, data).addCallback(newSendCallback());
            sendLatch.countDown();
        });
    }

}
