package com.ddf.spring.annotation.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author DDf on 2018/8/1
 * 实现InitializingBean接口的afterPropertiesSet方法在Bean被创建后会自动调用该初始化方法
 * 实现DisposableBean接口的destroy方法会在容器会Bean被销毁时时调用该方法
 */
public class InitAndDisposableBean implements InitializingBean, DisposableBean {
    public InitAndDisposableBean() {
        System.out.println("InitAndDisposableBean创建完成。。。。。。。。。。。。");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitAndDisposableBean创建后实现InitializingBean调用初始化方法。。。。。。。。。。。。");
    }
}
