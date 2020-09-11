package com.romtn.springexample.java.util;

import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        String reduce = Stream.generate(() -> "_").limit(8).reduce((x, y) -> x + y).get();
        System.out.println(reduce);
    }

}
