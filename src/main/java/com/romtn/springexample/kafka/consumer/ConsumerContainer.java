package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import com.romtn.springexample.kafka.config.Kafkas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ConsumerContainer implements Kafkas, Kafkas.GroupIds {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    KafkaConfig kafkaConfig;

    KafkaMessageListenerContainer<String, String> listenerContainer;

    AtomicInteger containerIndex = new AtomicInteger(0);

    private KafkaMessageListenerContainer<String, String> createContainer(ContainerProperties properties) {
        Map<String, Object> consumerConfigs = kafkaConfig.consumerConfigs();
        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<String, String>(consumerConfigs);
        return new KafkaMessageListenerContainer<>(factory, properties);
    }

    private void doStart(String[] topics, Object messageListener) {
        ContainerProperties properties = new ContainerProperties(topics);
        properties.setGroupId(myGroupId);
        properties.setMessageListener(messageListener);

        listenerContainer = createContainer(properties);
        listenerContainer.setBeanName("listener-container-" + containerIndex.getAndIncrement());

        listenerContainer.start();
        logger.info("new listener container start, properties:{} ......", properties);
    }

    public void restart(String[] topics, Object messageListener) {
        if (listenerContainer == null) {
            doStart(topics, messageListener);
            return;
        }

        logger.info("old listener container properties:{}", listenerContainer.getContainerProperties());

        listenerContainer.stop();
        logger.info("old listener container stop......");

        doStart(topics, messageListener);
    }

}
