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

    @KafkaListener(topics = {topics}, groupId = myGroupId)
    public void listen(ConsumerRecord<?, ?> record) throws Exception {
        record.topic();
        logger.info(record.toString());
    }

    @KafkaListener(topics = {test2}, groupId = myGroupId)
    public void listen2(ConsumerRecord<?, ?> record) throws Exception {
        record.topic();
        logger.info(record.toString());
    }

}
