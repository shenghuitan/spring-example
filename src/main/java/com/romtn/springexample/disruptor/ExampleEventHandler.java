package com.romtn.springexample.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.romtn.springexample.util.Counter;
import com.romtn.springexample.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleEventHandler implements EventHandler<ExampleEvent>, WorkHandler<ExampleEvent> {

    static Logger logger = LoggerFactory.getLogger(ExampleEventHandler.class);

    Counter counter = ExampleEventFactory.counter;
    Timer timer = ExampleEventFactory.timer;


    @Override
    public void onEvent(ExampleEvent event) throws Exception {
        counter.sent.getAndIncrement();
        if (counter.sent.get() >= counter.limit.get()) {
            timer.mark();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("onEvent event:{}", event);
        }
    }

    @Override
    public void onEvent(ExampleEvent event, long sequence, boolean endOfBatch) throws Exception {
        counter.sent.getAndIncrement();
        if (counter.sent.get() >= counter.limit.get()) {
            timer.mark();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("onEvent event:{}", event);
        }
    }
}
