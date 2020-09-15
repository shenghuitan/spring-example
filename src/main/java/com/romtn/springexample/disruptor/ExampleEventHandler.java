package com.romtn.springexample.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleEventHandler implements EventHandler<ExampleEvent>, WorkHandler<ExampleEvent> {

    static Logger logger = LoggerFactory.getLogger(ExampleEventHandler.class);

    @Override
    public void onEvent(ExampleEvent event) throws Exception {
        ExampleEventFactory.counter.sent.getAndIncrement();
        if (logger.isDebugEnabled()) {
            logger.debug("onEvent event:{}", event);
        }
    }

    @Override
    public void onEvent(ExampleEvent event, long sequence, boolean endOfBatch) throws Exception {
        ExampleEventFactory.counter.sent.getAndIncrement();
        if (logger.isDebugEnabled()) {
            logger.debug("onEvent event:{}", event);
        }
    }
}
