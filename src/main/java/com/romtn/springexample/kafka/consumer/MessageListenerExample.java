package com.romtn.springexample.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class MessageListenerExample implements MessageListener<String, String> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        logger.info("data:{}", data);
    }

}
