package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.Kafkas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitConsumer implements Kafkas.Topics, Kafkas.GroupIds {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConsumerContainer consumerContainer;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            String groupId = myGroupId;

            String topic = test0;
            consumerContainer.start(groupId, topic, new MessageListenerExample());
            logger.info("+++ init start, groupId:{}, topic:{}", groupId, topic);

            topic = test1;
            consumerContainer.start(groupId, topic, new BatchMessageListenerExample());
            logger.info("+++ init start, groupId:{}, topic:{}", groupId, topic);

            topic = test0;
            consumerContainer.stop(groupId, topic);
            logger.info("+++ init stop, groupId:{}, topic:{}", groupId, topic);
        }).start();
    }

}
