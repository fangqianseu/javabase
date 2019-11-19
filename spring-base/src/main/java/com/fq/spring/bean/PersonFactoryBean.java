package com.fq.spring.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author qianfang, at 2019-11-19, 16:41
 **/
public class PersonFactoryBean implements FactoryBean<Person> {
    //返回一个Person对象，这个对象会添加到容器中
    @Override
    public Person getObject() throws Exception {
        // TODO Auto-generated method stub
        System.out.println("ColorFactoryBean...getObject...");
        return new Person();
    }

    @Override
    public Class<?> getObjectType() {
        // TODO Auto-generated method stub
        return Person.class;
    }

    //true：这个bean是单实例，在容器中保存一份
    //false：多实例，每次获取都会创建一个新的bean；
    @Override
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return false;
    }
}
