package com.romtn.springexample.cglib;

import com.romtn.springexample.util.Abc;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

public class BeanCopy {

    public static void main(String[] args) {
        BeanCopier beanCopier = BeanCopier.create(Abc.class, Abc.class, false);
        Abc s = new Abc().setA("a").setB(1L).setC(2);
        Abc t = new Abc();

        beanCopier.copy(s, t, null);
        System.out.println(s);
        System.out.println(t);

        beanCopier.copy(t, s, null);
        System.out.println(s);
        System.out.println(t);

        BeanUtils.copyProperties(s, t);
        System.out.println(s);
        System.out.println(t);
    }

}
