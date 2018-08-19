package com.ddf.springmvc.annotation;

import com.ddf.springmvc.annotation.configuration.MyWebAppInitializer;
import org.springframework.web.SpringServletContainerInitializer;

import javax.servlet.ServletException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author DDf on 2018/8/19
 *
 * 如何使用main函数启动web环境？
 */
public class Application {
    public static void main(String[] args) throws ServletException {
        SpringServletContainerInitializer initializer = new SpringServletContainerInitializer();
        Set<Class<?>> set = new HashSet<>();
        // 1. 如何获取所有MyWebAppInitializer的子类？
        set.add(MyWebAppInitializer.class);
        // 2. 如何获取到ServletContext??
        initializer.onStartup(set, null);
    }
}
