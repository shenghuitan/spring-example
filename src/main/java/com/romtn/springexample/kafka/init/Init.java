package com.romtn.springexample.kafka.init;

import com.romtn.springexample.kafka.producer.*;
import com.romtn.springexample.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

@Service
public class Init {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NoProducerExample noProducerExample;

    @Autowired
    private SimpleProducerExample simpleProducerExample;

    @Autowired
    private NettyProducerExample nettyProducerExample;

    @Autowired
    private PoolProducerExample poolProducerExample;

    @Autowired
    private ReplyingProducerExample replyingProducerExample;

//    @PostConstruct
    public void init() {
        new Thread(() -> {
            Stream.of(
                    noProducerExample
                    , simpleProducerExample
                    , nettyProducerExample
                    , poolProducerExample
//                    , replyingProducerExample
            ).forEach(e -> execute(e));
        }).start();
    }

    private void execute(AbstractProducerExample producerExample) {
        Timer timer = Timer.init();
        for (int i = 0; i < 8; i++) {
            producerExample.execute();
            timer.mark();
        }
        logger.info("--- {} execute costs:{}ms", producerExample, timer.getCosts());
    }

}
