package com.ddf.springmvc.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/8/17
 */
@Service
public class HelloService {

    public String hello(String name) {
        return "hello " + name;
    }
}
