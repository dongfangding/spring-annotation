package com.ddf.springmvc.annotation.configuration.web;

import com.ddf.springmvc.annotation.configuration.interceptor.RequestContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author DDf on 2018/8/16
 * 创建web容器配置类，只扫描controller,并且配置与web相关的bean，
 * controllers, viewResolver, HandlerMapping等
 * useDefaultFilters默认为true，包含了@Componet, @Service, @Repository, @Controller等，
 *      因此需要设置为false，不需要扫描那么多类型
 *
 * 所有与SpringMVC相关的配置请参考
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config
 */
@ComponentScan(value = "com.ddf.springmvc.annotation", includeFilters = {
        @ComponentScan.Filter(value = Controller.class)
}, useDefaultFilters = false)
@EnableWebMvc
public class ServletApplicatioonContextConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestContextInterceptor requestContextInterceptor;

    public ServletApplicatioonContextConfig() {
        System.out.println("ServletApplicatioonContextConfig配置类被读取。。。。。。。。。。。。。");
    }


    /**
     * 为了支持转json，则需要导入jackson的包，否则返回对象时会提示找不到转换器，而不能成功转换为json。
     * 导入jsckson包就可以，未找到源码在哪里处理的这一块，导包就可以了
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
    }

    /**
     *  Add a resource handler for serving static resources based on the specified URL path
     * 	 * patterns. The handler will be invoked for every incoming request that matches to
     * 	 * one of the specified path patterns.
     * 	因为在web环境中最终所有的请求都会被dispatcherServlet拦截，然后由于配置了DefaultServletHandling,所以该映射如果
     * 	dispatcher处理不了，就会交由默认的servlet当做静态资源来处理。但是通过 addResourceHandlers可以直接添加静态资源的映射，
     * 	即前端发送的请求如果与静态资源映射请求相匹配，那么就可以明确本次是请求静态资源，然后再该映射配置的静态资源地址直接获得资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("addResourceHandlers...............");
        // 所有/static/** 相匹配的请求都到addResourceLocations指定的路径下获得资源
        // 当前项目请求/static/index.html则会找到/resource/static/index.html这个文件
        registry.addResourceHandler("/static/**").addResourceLocations("/static/",
                "classpath:static/");
    }

    /**
     * 通过调用new CorsRegistration(pathPattern);该类的config默认在实例化的如果没有配置允许的http方法，则会是所有的http方法，
     * 没有找到方法，如何自定义添加指定允许的http方法
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("addCorsMappings.................");
        // 这种默认是允许指定请求的所有的http方法
        registry.addMapping("/**");
    }

    /**
     *配置不经过handler的视图解析器，根据映射地址直接找指定视图，如下配置根据configureViewResolvers默认的视图解析的前缀和后缀，
     * 则配置了项目默认的初始化界面为/views/index.html
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("addViewControllers...............");
        registry.addViewController("/").setViewName("index");
    }


    /**
     * 配置SpringMVC的视图解析器
     * <bean id="viewResolver"
     * 		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
     * 		 <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
     * 		<property name="prefix" value="/views/" />
     * 		<property name="suffix" value=".html" />
     * 	</bean>
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        System.out.println("configureViewResolvers....................");
        registry.jsp("/views/", ".html");
    }


    /**
     * 开启静态资源的访问，否则所有的静态页面都不能访问
     * 即当静态资源的请求被SpringMVC拦截后并没有找到对应的映射，这时候应该将请求交给默认的Servlet去处理页面
     * 类似于原配置文件中 的
     * <mvc:default-servlet-handler/>
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        System.out.println("configureDefaultServletHandling.......................");
        configurer.enable();
    }


    /**
     * 注册拦截器列表
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("addInterceptors...................");
        registry.addInterceptor(requestContextInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
