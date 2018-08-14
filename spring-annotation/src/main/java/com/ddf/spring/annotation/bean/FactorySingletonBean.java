package com.ddf.spring.annotation.bean;

public class FactorySingletonBean {
    public FactorySingletonBean() {
        System.out.println("FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。");
    }
}