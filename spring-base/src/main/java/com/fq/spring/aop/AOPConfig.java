package com.fq.spring.aop;

import com.fq.spring.aop.LogAspects;
import com.fq.spring.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author qianfang, at 2019-11-19, 19:49
 **/
@Configuration
@EnableAspectJAutoProxy
public class AOPConfig {
    //业务逻辑类加入容器中
    @Bean
    public MathCalculator calculator() {
        return new MathCalculator();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
