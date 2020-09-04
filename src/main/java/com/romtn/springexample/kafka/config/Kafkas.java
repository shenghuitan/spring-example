package com.romtn.springexample.kafka.config;

public interface Kafkas {

    String BOOTSTRAP_SERVERS = "localhost:9092";

    interface Topics {
        String myTopic = "myTopic";
        String test2 = "test2";
    }

    interface GroupIds {
        String myGroupId = "myGroupId";
    }


}
