package com.fq.spring.config;

import com.fq.spring.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author qianfang, at 2019-11-19, 17:27
 **/
@Configuration
// 引入外部配置文件
@PropertySource("classpath:person.properties")
public class PropertyValuesConfig {
    @Bean
    public Person person() {
        return new Person();
    }
}
