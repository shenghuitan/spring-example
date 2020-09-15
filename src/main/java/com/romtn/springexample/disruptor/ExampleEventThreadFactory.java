package com.romtn.springexample.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ExampleEventThreadFactory implements ThreadFactory {

    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("example-event-poll-" + index.getAndIncrement());
        return thread;
    }
}
