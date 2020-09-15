package com.romtn.springexample.disruptor;

import com.lmax.disruptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExampleEventMultiTest {

    static Logger logger = LoggerFactory.getLogger(ExampleEventMultiTest.class);

    RingBuffer<ExampleEvent> ringBuffer;
    SequenceBarrier barrier;
    WorkHandler<ExampleEvent>[] handlers;
    WorkerPool<ExampleEvent> workerPool;
    Executor executor;
    ExampleEventTranslator translator;

    final int BUFFER_SIZE = 1 << 4;
    final int POOL_SIZE = 4;
    final long KEEP_ALIVE_TIME = 0L;

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
    }

    public void test() throws InterruptedException {
        for (int i = 0; ; i++) {
            ExampleEvent e = new ExampleEvent();
            e.setId(i);

            ExampleEventFactory.counter.received.getAndIncrement();
            ringBuffer.publishEvent(translator, e);
            logger.info("publishEvent event:{}", e);

            if (i % 1000 == 0) {
                Thread.sleep(1000);
                logger.info("counter:{}", ExampleEventFactory.counter);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("main start:{}", System.currentTimeMillis());

        ExampleEventMultiTest instance = new ExampleEventMultiTest();
        instance.init();
        instance.test();
    }

}
