package com.ddf.spring.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/19
 * 默认@Scope为singleton，IOC容器启动会创建该bean的实例，并且以后再次使用不会重新创建新的实例
 */
@Service
public class UserService {
    public UserService() {
        System.out.println("UserService创建完成...................");
    }

    public String welcome(String userName) {
        return "Hello " + userName;
    }

    public String welcomeException(String userName) {
        throw new RuntimeException("出现异常");
    }
}
