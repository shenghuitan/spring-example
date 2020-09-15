package com.romtn.springexample.disruptor;

import com.lmax.disruptor.*;
import com.romtn.springexample.util.Counter;
import com.romtn.springexample.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 测试：
 * 1、不多消费，不少消费
 */
public class ExampleEventMultiTest {

    static Logger logger = LoggerFactory.getLogger(ExampleEventMultiTest.class);

    final int BUFFER_SIZE = 1 << 4;
    final int POOL_SIZE = 4;
    final long KEEP_ALIVE_TIME = 0L;

    final long limit = 100_000_000;

    RingBuffer<ExampleEvent> ringBuffer;
    SequenceBarrier barrier;
    WorkHandler<ExampleEvent>[] handlers;
    WorkerPool<ExampleEvent> workerPool;
    ThreadPoolExecutor executor;
    ExampleEventTranslator translator;

    ThreadPoolExecutor producerExecutor;
    Counter counter = ExampleEventFactory.counter;

    Counter pool = new Counter();

    Timer timer = Timer.init();

    public void init() {
        ringBuffer = RingBuffer.createMultiProducer(new ExampleEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        barrier = ringBuffer.newBarrier();

        handlers = new ExampleEventHandler[POOL_SIZE];
        for (int i = 0; i < POOL_SIZE; i++) {
            handlers[i] = new ExampleEventHandler();
        }

        workerPool = new WorkerPool<>(ringBuffer, barrier, new ExampleEventExceptionHandler(), handlers);

        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new ExampleEventThreadFactory());

        workerPool.start(executor);

        translator = new ExampleEventTranslator();

        producerExecutor = new ThreadPoolExecutor(POOL_SIZE << 1, POOL_SIZE << 1,
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

    public static void main(String[] args) {
        logger.info("main start:{}", System.currentTimeMillis());

        ExampleEventMultiTest instance = new ExampleEventMultiTest();
        instance.init();
        instance.produce();
        instance.monitor();
    }

}
