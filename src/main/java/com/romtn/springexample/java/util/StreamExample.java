package com.romtn.springexample.java.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExample {

    public void reduce() {
        String reduce = Stream.generate(() -> "_").limit(8).reduce((x, y) -> x + y).get();
        System.out.println(reduce);
    }

    public void flatMap() {
        List<String> words = new ArrayList<>();
        words.add("hello");
        words.add("word");

        List<String> stringList = words.stream()
                .flatMap(word -> Arrays.stream(word.split("")))
                .distinct()
                .collect(Collectors.toList());
        stringList.forEach(e -> System.out.println(e));
    }

    public static void main(String[] args) {
        StreamExample instance = new StreamExample();
        instance.flatMap();
    }

}
