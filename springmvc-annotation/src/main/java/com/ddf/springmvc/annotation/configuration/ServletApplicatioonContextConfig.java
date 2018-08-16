package com.ddf.springmvc.annotation.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author DDf on 2018/8/16
 * 创建web容器配置类，只扫描controller,并且配置与web相关的bean，
 * controllers, viewResolver, HandlerMapping等
 * useDefaultFilters默认为true，包含了@Componet, @Service, @Repository, @Controller等，
 *      因此需要设置为false，不需要扫描那么多类型
 */
@ComponentScan(value = "com.ddf.springmvc.annotation", includeFilters = {
        @ComponentScan.Filter(value = Controller.class)
}, useDefaultFilters = false)
@EnableWebMvc
public class ServletApplicatioonContextConfig extends WebMvcConfigurerAdapter {

    public ServletApplicatioonContextConfig() {
        System.out.println("ServletApplicatioonContextConfig配置类被读取。。。。。。。。。。。。。");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("registry.addViewController(\"/heh\").setViewName(\"index\");");
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        System.out.println("configureViewResolvers");
        registry.jsp("/views", ".html");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
