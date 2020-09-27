package com.romtn.springexample.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.romtn.springexample.util.Abc;
import com.romtn.springexample.util.Abc2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JacksonExample {

    private static Logger logger = LoggerFactory.getLogger(JacksonExample.class);

    public static void copy() throws JsonProcessingException {
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

    public static void object() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String s = mapper.writeValueAsString(new Object());
        logger.info("s:{}", s);
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Abc s = new Abc().setA("a").setB(1L).setC(2).setD("3");
        String value = mapper.writeValueAsString(s);

        try {
            Map<String, Object> map1 = mapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
            logger.info("convertValue map:{}", map1);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        }

        try {
            Map<String, Object> map2 = mapper.readValue(value, new TypeReference<Map<String, Object>>() {});
            logger.info("readValue map:{}", map2);
        } catch (JsonProcessingException e) {
            logger.error("", e);
        }
    }
}
