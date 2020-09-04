package com.romtn.springexample.kafka.producer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import com.romtn.springexample.kafka.config.Kafkas;
import com.romtn.springexample.util.Timer;
import com.romtn.springexample.util.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ProducerExample implements Kafkas, Kafkas.Topics {

    Logger logger = LoggerFactory.getLogger(getClass());

    int threads = 8;

    ExecutorService executor = Executors.newFixedThreadPool(threads);

    @Autowired
    KafkaConfig kafkaConfig;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

//    @PostConstruct
    public void post() throws InterruptedException {
        send_10000();
    }

    public void send_10000() throws InterruptedException {
        kafkaTemplate.send(test2, Uuid.uuid());

        logger.info("+++ send_10000 start after 3 seconds");

        TimeUnit.SECONDS.sleep(3);

        final int size = 10_000_000;
        CountDownLatch latch = new CountDownLatch(size);

        Timer timer = Timer.init();

        int i = 0;
        for (; i < size; i++) {
            String uuid = Uuid.uuid();
            executor.execute(() -> {
                kafkaTemplate.send(test2, uuid);
                latch.countDown();
            });
        }

        latch.await();
        timer.mark();

        logger.info("--- i:{}, threads:{}, costs:{}ms, producerConfigs:{}", i, threads, timer.getCosts(),
                kafkaConfig.producerConfigs());
    }


    public void send() {
        this.kafkaTemplate.send(myTopic, "foo1");
        this.kafkaTemplate.send(myTopic, "foo2");
        this.kafkaTemplate.send(myTopic, "foo3");
        logger.info("send finish...");
    }

}
