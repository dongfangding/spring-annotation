package com.ddf.springmvc.annotation.configuration;

import com.ddf.springmvc.annotation.configuration.ioc.RootApplicationContextConfig;
import com.ddf.springmvc.annotation.configuration.web.ServletApplicatioonContextConfig;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import java.util.Set;

/**
 * @author DDf on 2018/8/16
 *
 * 基于注解版的SpringMVC的容器的配置，请参考官方文档
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-servlet-config
 *
 * 请参考org.springframework:spring-web的jar包下在META-INF/services下有一个文件叫javax.servlet.ServletContainerInitializer，里面配置的一个类
 * {@link SpringServletContainerInitializer}。这个类实现了{@link ServletContainerInitializer},所以同样具备
 * 容器一启动就会调用该类的{@link SpringServletContainerInitializer#onStartup(Set, ServletContext)}方法（Servlet3.0+机制）
 * 同时该类通过注解{@link javax.servlet.annotation.HandlesTypes}将所有实现{@link WebApplicationInitializer}
 * 接口的类都传入到onStartup()方法的第一个参数Set中,遍历所有的WebApplicationInitializer，把所有不是接口和子类的WebApplicationInitializer的添加到列表中，
 * 然后排序之后重新回调列表中的每个webApplicationInitializer的onStartup()方法
 *
 * webApplicationInitializer接口有三个子抽象类实现
 *  1. AbstractContextLoaderInitializer定义了启动之后创建registerContextLoaderListener。然后调用createRootApplicationContext()
 *      添加Spring根容器的Listener,createRootApplicationContext()这是一个抽象方法，因此是留给我们实现的
 *
 * 2. AbstractDispatcherServletInitializer继承了AbstractContextLoaderInitializer，在完成1的步骤后，然后调用
 *      registerDispatcherServlet()方法来new一个SpringMVC的前端控制器DispatcherServlet,然后调用方法getServletApplicationContextInitializers
 *      来创建SpringMVC的WEB容器，这个方法返回Null，。DispatcherServlet在这时还只是一个普通的类，在往下开始通过代码
 *      ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
 *      来将DispatcherServlet注册成一个Servlet.名称默认为dispatcher，而该Servlet的mapping则通过方法getServletMappings()获取，
 *      这个方法又是一个抽象方法，我们可以重写父类的这个方法来自定义映射规则；
 *      因此可以结合1，在1里只添加Spring IOC的核心容器，如service, dataSource，而
 *      在2里添加controllers, viewResolver, HandlerMapping等和web相关的bean
 *
 *  3. AbstractAnnotationConfigDispatcherServletInitializer继承AbstractDispatcherServletInitializer，它重写了父类的
 *    createRootApplicationContext()和createServletApplicationContext()方法，这两个方法内部分别通过获取配置类来创建Spring的
 *    根容器和Web容器，创建根容器的配置类通过getRootConfigClasses()这个方法获取，创建web容器的配置类通过getServletConfigClasses()
 *    方法获取，因此又留给我们自定义
 *
 *
 *  =============================================================
 *  综上所述，如果我们要基于配置类的方法来自定义创建Spring和SpringWebmvc的容器，则需要继承AbstractAnnotationConfigDispatcherServletInitializer
 *  这个类即可，而通过重写getRootConfigClasses()这个方法来指定IOC容器的配置类，getServletConfigClasses()这个方法
 *  来指定WEB容器的配置类，然后复写getServletMappings()方法指定DispatcherServlet的映射规则
 *
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * 指定Spring根容器的配置类
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootApplicationContextConfig.class};
    }

    /**
     * 指定WEB容器的配置类
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {ServletApplicatioonContextConfig.class};
    }

    /**
     * 配置SpringMVC的DispatcherServlet的映射规则
     * / 拦截所有请求，包括静态资源，但不包括JSP
     * /* 拦截所有资源，包含静态资源，包含JSP，一般JSP无法显示就是因为这里配置出错
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        System.out.println(getServletName() + "映射规则为: " + "/");
        return new String[] {"/"};
    }
}
