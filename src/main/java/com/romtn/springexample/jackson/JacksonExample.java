package com.romtn.springexample.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romtn.springexample.util.Abc;
import com.romtn.springexample.util.Abc2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonExample {

    private static Logger logger = LoggerFactory.getLogger(JacksonExample.class);

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Abc s = new Abc().setA("a").setB(1L).setC(2).setD("3");
        logger.info("s:{}", s);

        String value = mapper.writeValueAsString(s);
        logger.info("value:{}", value);

        Abc2 t = mapper.readValue(value, Abc2.class);
        logger.info("t:{}", t);

        Abc2 t2 = mapper.convertValue(s, Abc2.class);
        logger.info("t2:{}", t2);

        Abc s2 = mapper.convertValue(t2, Abc.class);
        logger.info("s2:{}", s2);
    }
}
