package com.fq.spring;

import com.fq.spring.bean.Person;
import com.fq.spring.tx.TXConfig;
import com.fq.spring.tx.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author qianfang, at 2019-11-19, 15:28
 **/
public class MainApp {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TXConfig.class);
        UserService bean = applicationContext.getBean(UserService.class);
        bean.insertUser();

        String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : namesForType) {
            System.out.println(name);
        }
    }
}
