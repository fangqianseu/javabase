package com.fq.spring.bean;

/**
 * @author qianfang, at 2019-11-19, 16:52
 **/
public class Car {
    public Car() {
        System.out.println("car constructor...");
    }

    public void init() {
        System.out.println("car ... init...");
    }

    public void detory() {
        System.out.println("car ... detory...");
    }
}
