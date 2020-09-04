package com.romtn.springexample.kafka.producer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import com.romtn.springexample.kafka.config.Kafkas;
import com.romtn.springexample.util.Timer;
import com.romtn.springexample.util.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ReplyingProducerExample implements Kafkas, Kafkas.Topics {

    Logger logger = LoggerFactory.getLogger(getClass());

    int threads = 8;

    ExecutorService executor = Executors.newFixedThreadPool(threads);

    @Autowired
    KafkaConfig kafkaConfig;

    @Autowired
    ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

//    @PostConstruct
    public void post() {
        new Thread(() -> {
            try {
                send_1();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }).start();
    }

    public void send_1() throws InterruptedException {
        replyingKafkaTemplate.send(test2, Uuid.uuid());

        logger.info("+++ send_1 start after 3 seconds");

        TimeUnit.SECONDS.sleep(3);

        final int size = 10_000_000;
        CountDownLatch latch = new CountDownLatch(size);

        Timer timer = Timer.init();

        int i = 0;
        for (; i < size; i++) {
            String uuid = Uuid.uuid();
            executor.execute(() -> {
                replyingKafkaTemplate.send(test2, uuid);
                latch.countDown();
            });
        }

        latch.await();
        timer.mark();

        logger.info("--- send_1 i:{}, threads:{}, costs:{}ms, producerConfigs:{}", i, threads, timer.getCosts(),
                kafkaConfig.producerConfigs());
    }


}
