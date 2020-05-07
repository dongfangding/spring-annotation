package com.ddf.annotation.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author DDf on 2018/8/14
 * 监听servlet容器的创建和销毁
 */
public class CustomListeners implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("监听容器初始化。。。。。。。。。");
        System.out.println(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("监听容器销毁..........");
        System.out.println(servletContextEvent);
    }
}
