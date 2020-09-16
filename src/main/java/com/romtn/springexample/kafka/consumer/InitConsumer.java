package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.Kafkas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitConsumer implements Kafkas.Topics {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConsumerContainer consumerContainer;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            consumerContainer.restart(new String[]{test0}, new MessageListenerExample());
            logger.info("+++ init restart, properties:{}", consumerContainer.listenerContainer.getContainerProperties());

            consumerContainer.restart(new String[]{test0, test1}, new BatchMessageListenerExample());
            logger.info("--- init restart, properties:{}", consumerContainer.listenerContainer.getContainerProperties());
        }).start();
    }

}
