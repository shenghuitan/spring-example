package com.romtn.springexample.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class BMap<K, V> {

    Map<K, V> map = new LinkedHashMap<>();

    public BMap<K, V> put(K k, V v) {
        map.put(k, v);
        return this;
    }

    public Map<K, V> getMap() {
        return map;
    }

}
