package com.romtn.springexample.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.romtn.springexample.util.Counter;
import com.romtn.springexample.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExampleEventSingleTest {

    static Logger logger = LoggerFactory.getLogger(ExampleEventSingleTest.class);

    RingBuffer<ExampleEvent> ringBuffer;
    ExampleEventTranslator translator;

    final int BUFFER_SIZE = 1024;

    ThreadPoolExecutor producerExecutor;

    Counter pool = new Counter();
    Counter counter = ExampleEventFactory.counter;

    Timer timer = Timer.init();


    public void init() {
        Disruptor<ExampleEvent> disruptor = new Disruptor<>(
                new ExampleEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith(new ExampleEventHandler());
        disruptor.start();

        ringBuffer = disruptor.getRingBuffer();

        translator = new ExampleEventTranslator();

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

            ringBuffer.publishEvent(translator, e);
            if (logger.isDebugEnabled()) {
                logger.debug("publishEvent event:{}", e);
            }
        }
        timer.mark();
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

    public static void main(String[] args) throws InterruptedException {
        logger.info("main start:{}", System.currentTimeMillis());

        ExampleEventSingleTest instance = new ExampleEventSingleTest();
        instance.init();
        instance.produce();
        instance.monitor();
    }

}
