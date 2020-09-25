package com.romtn.springexample.util;

import java.io.Serializable;

public class Abc2 implements Serializable {

    private String a;
    private Long b;
    private Integer c;
    private Long d;

    public String getA() {
        return a;
    }

    public Abc2 setA(String a) {
        this.a = a;
        return this;
    }

    public Long getB() {
        return b;
    }

    public Abc2 setB(Long b) {
        this.b = b;
        return this;
    }

    public Integer getC() {
        return c;
    }

    public Abc2 setC(Integer c) {
        this.c = c;
        return this;
    }

    public Long getD() {
        return d;
    }

    public Abc2 setD(Long d) {
        this.d = d;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Abc2{");
        sb.append("a='").append(a).append('\'');
        sb.append(", b=").append(b);
        sb.append(", c=").append(c);
        sb.append(", d=").append(d);
        sb.append('}');
        return sb.toString();
    }
}
