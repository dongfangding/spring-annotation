package com.ddf.springmvc.annotation.controller;

import com.ddf.springmvc.annotation.configuration.util.RequestContext;
import com.ddf.springmvc.annotation.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DDf on 2018/8/17
 */
@RestController
@RequestMapping("hello")
public class HelloController {
    @Autowired
    private HelloService helloService;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/string")
    public String string(@RequestParam String name) {
        System.out.println(requestContext.getParamsMap());
        return helloService.hello(name);
    }


    /**
     * 需要导入jackson的包，则会返回json
     * @param name
     * @return
     */
    @RequestMapping("/json")
    public Map json(@RequestParam String name) {
        System.out.println(requestContext.getParamsMap());
        Map<String, Object> map = new HashMap<>();
        map.put("msg", helloService.hello(name));
        return map;
    }


    /**
     * 在当前的环境中有两个ApplicationContext,一个是根据RootApplicationContextConfig建立的Root ApplicationContext，
     * 一个是根据ServletApplicatioonContextConfig创建的ApplicationContext。 Root ApplicationContext是跟容器，是与spring相关的
     * bean都被管理在这个容器中，而与web相关的容器都被管理在ApplicationContext中（这一块没有强制要求，根据代码会有不同的结果，但推荐这种分离的写法）
     *
     * 两个ApplicationContext都是由org.springframework.web.context.support.AnnotationConfigWebApplicationContext 实现的
     */
    @RequestMapping("application")
    public void application() {
        System.out.println(applicationContext.getClass());
        System.out.println(applicationContext.getParent().getClass());
        System.out.println(applicationContext==applicationContext.getParent());

        // 这个会打印所有在web容器中注册的bean
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("root application context。。。。。。。。。。。。。。。。。。。。。。。");

        // 这个会打印所有在Root IOC容器中注册的bean
        String[] names1 = applicationContext.getParent().getBeanDefinitionNames();
        for (String name : names1) {
            System.out.println(name);
        }
    }
}
