package com.ddf.springmvc.annotation.configuration.ioc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/8/16
 * 指定Spring根容器的配置类，排除web相关的bean
 */
@ComponentScan(value = "com.ddf.springmvc.annotation",
        excludeFilters = {@ComponentScan.Filter(value = Controller.class)})
public class RootApplicationContextConfig {

    public RootApplicationContextConfig() {
        System.out.println("RootApplicationContextConfig配置类被读取。。。。。。。。。。。。。。");
    }
}
