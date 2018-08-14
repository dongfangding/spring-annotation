package com.ddf.annotation.servlet.configuration;

import com.ddf.annotation.servlet.CustomServlet;
import com.ddf.annotation.servlet.filter.CustomFilter;
import com.ddf.annotation.servlet.listener.CustomListeners;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author DDf on 2018/8/14
 * @HandlesTypes 容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来作为onStartup的第一个参数
 *
 * 1、Servlet容器启动会扫描，当前应用里面每一个jar包的
 * 	ServletContainerInitializer的实现
 * 2、提供ServletContainerInitializer的实现类；
 * 	必须绑定在classpath下，META-INF/services/javax.servlet.ServletContainerInitializer
 * 	文件的内容就是ServletContainerInitializer实现类的全类名；
 */
@HandlesTypes(value={Filter.class})
public class CustomsContainerInit implements ServletContainerInitializer {


    /**
     * 在容器启动的时候创建servlet组件，不需要使用配置文件
     * @param set
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("@HandlesTypes传入的类型有: ");
        for (Class clazz : set) {
            System.out.println(clazz);
        }

        // 注册Servlet
        ServletRegistration.Dynamic customServlet = servletContext.addServlet("customServlet", new CustomServlet());
        customServlet.addMapping("/custom");

        // 注册Listener
        servletContext.addListener(CustomListeners.class);

        // 注册Filter
        FilterRegistration.Dynamic customFilter = servletContext.addFilter("customFilter", new CustomFilter());
        customFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

    }
}
