package com.romtn.springexample.disruptor;

import com.lmax.disruptor.ExceptionHandler;

public class ExampleEventExceptionHandler implements ExceptionHandler<ExampleEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, ExampleEvent event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
