package com.romtn.springexample.kafka.config;

public interface Kafkas {

//    String BOOTSTRAP_SERVERS = "localhost:9092";
//    String BOOTSTRAP_SERVERS = "kafkafs001-test001-pub.yy.com:8101,kafkafs001-test002-pub.yy.com:8101,kafkafs001-test003-pub.yy.com:8101";

    String topics = "#{'${spring.kafka.consumer.topics}'.split(',')}";

    interface Topics {
        String myTopic = "myTopic";
        String test = "test";
        String test0 = "test0";
        String test1 = "test1";
        String test2 = "test2";
    }

    interface GroupIds {
        String myGroupId = "myGroupId";
    }


}
