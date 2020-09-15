package com.romtn.springexample.disruptor;

import com.lmax.disruptor.EventFactory;
import com.romtn.springexample.util.Counter;

public class ExampleEventFactory implements EventFactory<ExampleEvent> {

    public static Counter counter = new Counter();

    @Override
    public ExampleEvent newInstance() {
        return new ExampleEvent();
    }

}
