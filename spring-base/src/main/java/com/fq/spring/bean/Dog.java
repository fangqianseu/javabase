package com.fq.spring.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author qianfang, at 2019-11-19, 17:07
 **/
public class Dog {
    public Dog() {
        System.out.println("dog constructor...");
    }

    //对象创建并赋值之后调用
    @PostConstruct
    public void init() {
        System.out.println("Dog....@PostConstruct...");
    }

    //容器移除对象之前
    @PreDestroy
    public void detory() {
        System.out.println("Dog....@PreDestroy...");
    }
}
