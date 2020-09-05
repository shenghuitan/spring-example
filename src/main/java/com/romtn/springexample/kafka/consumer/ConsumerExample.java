package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.Kafkas;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerExample implements Kafkas, Kafkas.Topics, Kafkas.GroupIds {

    Logger logger = LoggerFactory.getLogger(getClass());

//    @KafkaListener(topics = myTopic, groupId = myGroupId)
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info(cr.toString());
    }

}
