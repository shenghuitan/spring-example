package com.romtn.springexample.disruptor;

import com.lmax.disruptor.EventFactory;
import com.romtn.springexample.util.Counter;
import com.romtn.springexample.util.Timer;

public class ExampleEventFactory implements EventFactory<ExampleEvent> {

    public static Counter counter = new Counter();
    public static Timer timer = Timer.init();

    @Override
    public ExampleEvent newInstance() {
        counter.length.getAndIncrement();
        return new ExampleEvent();
    }

}
