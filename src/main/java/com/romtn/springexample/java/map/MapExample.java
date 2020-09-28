package com.romtn.springexample.java.map;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapExample {

    public static void main(String[] args) {
        Map<String, Object> map = new LinkedHashMap<String, Object>(){{
            put("a", 1);
            put("b", "2");
            put("c", new Object());
            put("d", new MapExample());
        }};
        System.out.println(map);
    }

}
