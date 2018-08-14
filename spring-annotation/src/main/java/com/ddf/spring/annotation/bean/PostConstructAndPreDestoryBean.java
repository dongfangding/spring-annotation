package com.ddf.spring.annotation.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author DDf on 2018/8/1
 * 使用JSR250注解实现Bean的初始化方法和销毁方法调用
 */
public class PostConstructAndPreDestoryBean {
    public PostConstructAndPreDestoryBean() {
        System.out.println("PostConstructAndPreDestoryBean创建完成.......");
    }

    /**
     * 使用@PostConstruct指定Bean的初始化方法
     */
    @PostConstruct
    public void init() {
        System.out.println("PostConstructAndPreDestoryBean创建完成，使用@PostConstruct注解来调用初始化方法。。。。");
    }


    /**
     * 使用@PreDestroy指定Bean销毁后调用的方法
     */
    @PreDestroy
    public void destroy() {
        System.out.println("PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。");
    }
}
