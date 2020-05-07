package com.ddf.spring.annotation.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/21
 * 测试@Scope的作用域为prototype，每次使用该bean都会重新生成一个实例
 */
@Service
@Scope("prototype")
public class PrototypeScopeService {
    public PrototypeScopeService() {
        System.out.println("PrototypeScopeService创建完成。。。。。。。。");
    }
}
