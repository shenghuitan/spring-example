package com.romtn.springexample.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleEventSingleTest {

    static Logger logger = LoggerFactory.getLogger(ExampleEventSingleTest.class);

    RingBuffer<ExampleEvent> ringBuffer;
    ExampleEventHandler[] handlers;
    ExampleEventTranslator translator;

    final int BUFFER_SIZE = 1024;

//    @Before
    public void init() {
        handlers = new ExampleEventHandler[2];
        handlers[0] = new ExampleEventHandler();
        handlers[1] = new ExampleEventHandler();

        Disruptor<ExampleEvent> disruptor = new Disruptor<>(new ExampleEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith(handlers[0]);
        disruptor.start();

        ringBuffer = disruptor.getRingBuffer();

        translator = new ExampleEventTranslator();
    }

//    @Test
    public void test() throws InterruptedException {
        for (int i = 0; ; i++) {
            ExampleEvent e = new ExampleEvent();
            e.setId(i);

            ringBuffer.publishEvent(translator, e);
            logger.info("publishEvent event:{}", e);

            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("main start:{}", System.currentTimeMillis());

        ExampleEventSingleTest instance = new ExampleEventSingleTest();
        instance.init();
        instance.test();

    }

}
