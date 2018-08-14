package com.ddf.spring.annotation.bean;

import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/6
 */
@Component
public class AutowiredBean {
    public AutowiredBean() {
        System.out.println("AutowiredBean创建完成。。。。。。。。。。。。");
    }
}
