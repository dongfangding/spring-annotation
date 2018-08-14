package com.ddf.spring.annotation.controller;

import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * 该类的作用是为了测试排除所有@Controller注解的类不需要注册到IOC容器中，
 * 详见{@link com.ddf.spring.annotation.configuration.AnnotationConfiguration}
 */
@Controller
public class UserController {
    public UserController() {
        System.out.println("UserController创建完成.................");
    }
}
