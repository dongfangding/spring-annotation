package com.ddf.spring.annotation.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/21
 * 测试单实例bean的@Lazy IOC容器启动的时候不创建该bean，只有到第一次获取的时候才创建
 * 但当第一次使用这个bean的时候再创建，以后使用不再创建
 */
@Service
@Lazy
public class LazyBeanService {
    public LazyBeanService() {
        System.out.println("LazyBeanService创建完成...............");
    }
}




