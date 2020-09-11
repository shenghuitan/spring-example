package com.romtn.springexample.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Function;

@Service
public class FunctionExample {

    private static Logger logger = LoggerFactory.getLogger(FunctionExample.class);

    private Function<Long, Long> m2 = t -> t * 2;

    private Function<Long, Long> m3 = t -> t * 3;

    private Function<String, String> a0 = t -> t + "0";
    private Function<String, String> a1 = t -> t + "1";

    @PostConstruct
    public void init() {
        logger.info("--- m2: {}", m2.apply(1L));            // 2
        logger.info("m2: {}", m2.compose(m3).apply(2L));    // 2 * 3 * 2 = 12
        logger.info("m2: {}", m2.andThen(m3).apply(3L));    // 3 * 2 * 3 = 18

        logger.info("--- append: {}", a0.apply("a"));           // a0
        logger.info("append: {}", a0.compose(a1).apply("b"));   // b10
        logger.info("append: {}", a0.andThen(a1).apply("c"));   // c01
    }


}
