package com.romtn.springexample.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Timer {

    private List<Long> times;

    private Timer() {
        times = new ArrayList<>();
        times.add(System.nanoTime());
    }

    public static Timer init() {
        return new Timer();
    }

    public Timer mark() {
        times.add(System.nanoTime());
        return this;
    }

    public List<Long> getTimes() {
        return times;
    }

    public List<Long> getCosts() {
        List<Long> millis = new ArrayList<>(times.size());
        Iterator<Long> iterator = times.iterator();
        Long last = iterator.next();
        while (iterator.hasNext()) {
            millis.add((iterator.next() - last) / 1_000_000);
        }
        return millis;
    }
}
