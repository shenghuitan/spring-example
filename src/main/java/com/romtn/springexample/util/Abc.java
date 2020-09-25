package com.romtn.springexample.util;

import java.io.Serializable;

public class Abc implements Serializable {

    private String a;
    private Long b;
    private Integer c;

    public String getA() {
        return a;
    }

    public Abc setA(String a) {
        this.a = a;
        return this;
    }

    public Long getB() {
        return b;
    }

    public Abc setB(Long b) {
        this.b = b;
        return this;
    }

    public Integer getC() {
        return c;
    }

    public Abc setC(Integer c) {
        this.c = c;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Abc{");
        sb.append("a='").append(a).append('\'');
        sb.append(", b=").append(b);
        sb.append(", c=").append(c);
        sb.append('}');
        return sb.toString();
    }
}
