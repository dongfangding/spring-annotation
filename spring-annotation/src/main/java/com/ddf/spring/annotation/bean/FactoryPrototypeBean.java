package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/8/1
 * 该类用于测试使用FactoryBean来注册每个请求重新创建组件
 */
public class FactoryPrototypeBean {
    public FactoryPrototypeBean() {
        System.out.println("FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。");
    }
}