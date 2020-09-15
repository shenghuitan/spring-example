package com.romtn.springexample.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleEventTranslator implements EventTranslatorOneArg<ExampleEvent, ExampleEvent> {

    static Logger logger = LoggerFactory.getLogger(ExampleEventTranslator.class);

    @Override
    public void translateTo(ExampleEvent event, long sequence, ExampleEvent arg0) {
        ExampleEventFactory.counter.read.getAndIncrement();
        event.setId(arg0.getId());
        logger.info("translateTo arg0:{}", arg0);
    }

}
