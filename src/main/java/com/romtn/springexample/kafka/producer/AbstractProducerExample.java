package com.romtn.springexample.kafka.producer;

import com.romtn.springexample.kafka.config.KafkaConfig;
import com.romtn.springexample.kafka.config.Kafkas;
import com.romtn.springexample.util.BMap;
import com.romtn.springexample.util.Timer;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractProducerExample implements Kafkas.Topics {

    Logger logger = LoggerFactory.getLogger(getClass());

    protected long WAIT_MILLIS = 0;     // 测试启动等待时间
    protected long LOGIC_MILLIS = 1;    // 逻辑处理时间

    final String topic = test1;

    int threads = 2;    // 执行线程数

    final int size = 10_000;
    CountDownLatch sendLatch = new CountDownLatch(size);
    CountDownLatch replyLatch = new CountDownLatch(size);
    AtomicInteger failCount = new AtomicInteger(0);

    protected final int KEY_LENGTH = 32;
    protected final int DATA_LENGTH = 1024;

    @Autowired
    KafkaConfig kafkaConfig;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    protected void send() throws InterruptedException {
        logger.info("+++ send start");

        wait0();

        Timer sendTimer = Timer.init();
        Timer replyTimer = Timer.init();

        int i = 0;
        long bytes = 0;

        for (; i < size; i++) {
            String key = randomKey();
            String data = randomData();
            bytes += key.getBytes().length + data.getBytes().length;

            logic0();

            send0(key, data);
        }

        sendLatch.await();
        sendTimer.mark();

        replyLatch.await();
        replyTimer.mark();

        Map<String, Object> map = new BMap<String, Object>()
                .put("size", size)
                .put("thread", threads)
                .put("bytes", bytes / 1024 / 1024 + "MB")
                .put("avg", bytes / size + "B")
                .put("send.cost", sendTimer.getCosts() + "ms")
                .put("reply.cost", replyTimer.getCosts() + "ms")
                .put("failCount", failCount)
                .put("producerConfigs", kafkaConfig.producerConfigs())
//                .put("producer.metrics", getKafkaTemplate().metrics())
                .getMap();

        logger.info("--- send end, {}", map);
    }

    protected abstract void send0(String key, String data);

    public void execute() {
        try {
            send();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected String randomData() {
        return new StringBuilder(DATA_LENGTH + 32)
                .append("{\"data\":\"").append(RandomString.make(DATA_LENGTH)).append("\"}")
                .toString();
    }

    protected String randomKey() {
        return RandomString.make(KEY_LENGTH);
    }

    protected void wait0() throws InterruptedException {
        if (WAIT_MILLIS > 0) {
            TimeUnit.MILLISECONDS.sleep(WAIT_MILLIS);
        }
    }

    protected void logic0() throws InterruptedException {
        if (LOGIC_MILLIS > 0) {
            TimeUnit.MILLISECONDS.sleep(LOGIC_MILLIS);
        }
    }

    protected <T> ListenableFutureCallback<T> newSendCallback() {
        return new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable ex) {
                replyLatch.countDown();
                failCount.incrementAndGet();
            }
            @Override
            public void onSuccess(T result) {
                replyLatch.countDown();
            }
        };
    }

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return kafkaTemplate;
    }
}
