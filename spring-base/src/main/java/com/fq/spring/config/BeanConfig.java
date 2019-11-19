package com.fq.spring.config;

import com.fq.spring.bean.Person;
import com.fq.spring.bean.PersonFactoryBean;
import com.fq.spring.condition.MyCondition;
import com.fq.spring.condition.MyImportBeanDefinitionRegistrar;
import com.fq.spring.condition.MyImportSelector;
import com.fq.spring.condition.MyTypeFilter;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author qianfang, at 2019-11-19, 15:29
 **/

// 也可判断配置类
@Conditional({MyCondition.class})

@Configuration
@ComponentScan(value = "com.fq.spring",
        // 排除某些类
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)})

// 包含某些类 需要先 useDefaultFilters = false
@ComponentScan(useDefaultFilters = false,
        //    按照class过滤
//        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Person.class)})
        // 按照 自定义规则 过滤
        includeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyTypeFilter.class)})

/**
 * @Import[快速给容器中导入一个组件]
 *      1）、@Import(要导入到容器中的组件)；容器中就会自动注册这个组件，id默认是全类名
 * 		2）、ImportSelector:返回需要导入的组件的全类名数组；
 * 		3）、ImportBeanDefinitionRegistrar:手动注册bean到容器中
 */
@Import({Person.class,
        MyImportSelector.class,
        MyImportBeanDefinitionRegistrar.class})
public class BeanConfig {

    @Bean(value = "new_Person")
    /**
     * @Scope:调整作用域
     * prototype：多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。
     * 					每次获取的时候才会调用方法创建对象；
     * singleton：单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。
     * 			以后每次获取就是直接从容器（map.get()）中拿，
     * request：同一次请求创建一个实例
     * session：同一个session创建一个实例
     *
     */
    @Scope(value = "prototype")
    /**
     * 懒加载：
     * 		单实例bean：默认在容器启动的时候创建对象；
     * 		懒加载：容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化
     */
    @Lazy
    public Person newPerson() {
        return new Person("fq", 18, "FQ").setNickName("FQ");
    }

    /**
     * @Conditional({Condition}) ： 按照自定义条件进行判断
     */
    @Conditional(MyCondition.class)
    @Bean("linus")
    public Person person02() {
        return new Person("linus", 48, "LINUX");
    }

    /**
     * 工厂bean 获取的是 person对象
     * 若要工厂对象 则 getbean("&personFactoryBean")
     * @return
     */
    @Bean("personFactoryBean")
    public PersonFactoryBean personFactoryBean() {
        return new PersonFactoryBean();
    }
}
