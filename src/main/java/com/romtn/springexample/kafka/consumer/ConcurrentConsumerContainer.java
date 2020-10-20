package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class ConcurrentConsumerContainer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaConfig kafkaConfig;

    /**
     * key: groupId
     * subKey: topic
     * value:  listenerContainer
     */
    private Map<String, Map<String, ConcurrentMessageListenerContainer<String, String>>> listenerContainersMap = new ConcurrentSkipListMap<>();

    private ConcurrentMessageListenerContainer<String, String> createContainer(ContainerProperties properties) {
        ConsumerFactory<String, String> consumerFactory = kafkaConfig.consumerFactory();
        return new ConcurrentMessageListenerContainer<>(consumerFactory, properties);
    }

    public void start(String groupId, String topic, Object messageListener) {
        Map<String, ConcurrentMessageListenerContainer<String, String>> containers =
                listenerContainersMap.computeIfAbsent(groupId, (k) -> new ConcurrentHashMap<>());

        ConcurrentMessageListenerContainer<String, String> container = containers.computeIfAbsent(topic, t -> {
            ContainerProperties properties = new ContainerProperties(topic);
            properties.setGroupId(groupId);
            properties.setMessageListener(messageListener);

            ConcurrentMessageListenerContainer<String, String> container0 = createContainer(properties);
            container0.setBeanName(String.format("listener-container-%s-%s", topic, groupId));

            return container0;
        });

        container.start();
        logger.info("listener container start, topic:{}, groupId:{}", topic, groupId);
    }

    public void stop(String groupId, String topic) {
        ConcurrentMessageListenerContainer<String, String> container = getContainer(groupId, topic);
        if (container == null) {
            logger.info("listenerContainer topic not found, topic:{}, groupId:{}", topic, groupId);
            return;
        }

        container.stop();
        logger.info("listener container stop, topic:{}, groupId:{}", topic, groupId);
    }

    public ConcurrentMessageListenerContainer<String, String> getContainer(String groupId, String topic) {
        Map<String, ConcurrentMessageListenerContainer<String, String>> containers = listenerContainersMap.get(groupId);
        if (containers == null) {
            logger.info("listenerContainers groupId not found, topic:{}, groupId:{}", topic, groupId);
            return null;
        }

        ConcurrentMessageListenerContainer<String, String> container = containers.get(topic);
        if (container == null) {
            logger.info("listenerContainer topic not found, topic:{}, groupId:{}", topic, groupId);
            return null;
        }

        return container;
    }

}
