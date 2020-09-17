package com.romtn.springexample.kafka.consumer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class ConsumerContainer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    KafkaConfig kafkaConfig;

    /**
     * key: groupId
     * subKey: topic
     * value:  listenerContainer
     */
    Map<String, Map<String, KafkaMessageListenerContainer<String, String>>> listenerContainersMap = new ConcurrentSkipListMap<>();

    private KafkaMessageListenerContainer<String, String> createContainer(ContainerProperties properties) {
        Map<String, Object> consumerConfigs = kafkaConfig.consumerConfigs();
        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerConfigs);
        return new KafkaMessageListenerContainer<>(factory, properties);
    }

    public void start(String groupId, String topic, Object messageListener) {
        Map<String, KafkaMessageListenerContainer<String, String>> containers =
                listenerContainersMap.computeIfAbsent(groupId, (k) -> new ConcurrentHashMap<>());

        KafkaMessageListenerContainer<String, String> container = containers.computeIfAbsent(topic, t -> {
            ContainerProperties properties = new ContainerProperties(topic);
            properties.setGroupId(groupId);
            properties.setMessageListener(messageListener);

            KafkaMessageListenerContainer<String, String> container0 = createContainer(properties);
            container0.setBeanName(String.format("listener-container-%s-%s", topic, groupId));

            return container0;
        });

        container.start();
        logger.info("listener container start, topic:{}, groupId:{}", topic, groupId);
    }

    public void stop(String groupId, String topic) {
        KafkaMessageListenerContainer<String, String> container = getContainer(groupId, topic);
        if (container == null) {
            logger.info("listenerContainer topic not found, topic:{}, groupId:{}", topic, groupId);
            return;
        }

        container.stop();
        logger.info("listener container stop, topic:{}, groupId:{}", topic, groupId);
    }

    public KafkaMessageListenerContainer<String, String> getContainer(String groupId, String topic) {
        Map<String, KafkaMessageListenerContainer<String, String>> containers = listenerContainersMap.get(groupId);
        if (containers == null) {
            logger.info("listenerContainers groupId not found, topic:{}, groupId:{}", topic, groupId);
            return null;
        }

        KafkaMessageListenerContainer<String, String> container = containers.get(topic);
        if (container == null) {
            logger.info("listenerContainer topic not found, topic:{}, groupId:{}", topic, groupId);
            return null;
        }

        return container;
    }

}
