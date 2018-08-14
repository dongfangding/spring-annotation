package com.ddf.spring.annotation.service;

import com.ddf.spring.annotation.bean.AutowiredBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author DDf on 2018/8/6
 * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
 *      * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
 *      * 默认注入@Primary修饰的Bean，如果Bean不一定存在，可以使用属性required=false，则Bean不存在也不会抛出异常
 * 可以标注在构造器，方法，参数，字段上
 *
 * @Resource 默认使用名称注入Bean,可以使用name属性指定具体要注入的Bean的名称，不支持@Primary，不支持required=false,不支持@Primary
 * @Inject  使用类型去注入，支持@Primary，不支持required=false
 *
 *
 */
@Service
public class AutowiredService {
    @Autowired
    private AutowiredBean autowiredBean;
    public AutowiredBean getAutowiredBean() {
        return autowiredBean;
    }

    @Autowired
    @Qualifier("autowiredBean")
    private AutowiredBean qualifierAutowiredBean;
    public AutowiredBean getQualifierAutowiredBean() {
        return qualifierAutowiredBean;
    }

    @Resource(name = "autowiredBean")
    private AutowiredBean resourceAutowiredBean;
    public AutowiredBean getResourceAutowiredBean() {
        return resourceAutowiredBean;
    }


    @Resource(name = "autowiredBean2")
    private AutowiredBean resourceAutowiredBean2;
    public AutowiredBean getResourceAutowiredBean2() {
        return resourceAutowiredBean2;
    }

    @Inject
    private UserService userService;
    public UserService getUserService() {
        return userService;
    }

    public AutowiredService() {
        System.out.println("AutowiredService创建完成。。。。。。。。。。。。");
    }
}
