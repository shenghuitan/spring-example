package com.romtn.springexample.disruptor;

import com.romtn.springexample.util.Counter;
import com.romtn.springexample.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueueTest {

    static Logger logger = LoggerFactory.getLogger(LinkedBlockingQueueTest.class);

    LinkedBlockingQueue<ExampleEvent> queue = new LinkedBlockingQueue<>();

    final int POOL_SIZE = 1;
    final long KEEP_ALIVE_TIME = 0L;

    final long limit = 100_000_000;

    ThreadPoolExecutor producerExecutor;
    ExampleEventHandler handler = new ExampleEventHandler();

    Counter pool = new Counter();
    Counter counter = ExampleEventFactory.counter;

    Timer timer = ExampleEventFactory.timer;


    public void init() {
        producerExecutor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("producer-pool-" + pool.received.getAndIncrement());
                    return thread;
                });

        counter.limit.set(limit);
        timer.mark();
    }

    public void produce() {
        for (int i = 0; i < producerExecutor.getMaximumPoolSize(); i++) {
            producerExecutor.execute(() -> produce0());
        }
    }

    public void produce0() {
        for (int i = 0; counter.received.get() < counter.limit.get(); i++) {
            counter.received.getAndIncrement();

            ExampleEvent e = new ExampleEvent();
            e.setId(i);

            queue.offer(new ExampleEvent().setId(i));
            if (logger.isDebugEnabled()) {
                logger.debug("publishEvent event:{}", e);
            }
        }
        timer.mark();
    }

    public void consume() {
        new Thread(() -> {
            for (; ; ) {
                ExampleEvent event = queue.poll();
                if (event != null) {
                    counter.read.getAndIncrement();
                    try {
                        handler.onEvent(event);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        }).start();
    }

    public void monitor() {
        new Thread(() -> {
            for (; ; ) {
                if (counter.received.get() < counter.limit.get()) {
                    timer.mark();
                }

                logger.info("counter:{}, timer:{}, costs:{}", counter, timer, timer.costs());

                if (counter.sent.get() >= counter.limit.get()) {
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            System.exit(0);
        }).start();
    }

    public static void main(String[] args) {
        logger.info("main start:{}", System.currentTimeMillis());

        LinkedBlockingQueueTest instance = new LinkedBlockingQueueTest();
        instance.init();
        instance.produce();
        instance.consume();
        instance.monitor();
    }

}
