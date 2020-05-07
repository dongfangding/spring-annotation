package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.*;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 *
 * @EnableAspectJAutoProxy 开启基于注解的aop模式
 *
 * @EnableTransactionManagement 开启基于注解的事务支持
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * initMethod 指定Bean创建后调用的初始化方法
     * destroyMethod 指定Bean在销毁后会调用的方法
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destory")
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean developmentService() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean productionService() {
        return new ProductionBean();
    }


    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactoryPrototypeBeanConfiguration factoryPrototypeBeanConfiguration() {
        return new FactoryPrototypeBeanConfiguration();
    }

    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactorySingletonBeanConfiguration factorySingletonBeanConfiguration() {
        return new FactorySingletonBeanConfiguration();
    }


    /**
     * 注册一个实现InitializingBean, DisposableBean接口来指定Bean的初始化和销毁方法的Bean
     * @return
     */
    @Bean
    public InitAndDisposableBean initAndDisposableBean() {
        return new InitAndDisposableBean();
    }

    /**
     * 创建一个通过JSR250 @PostConstruct指定初始化方法/@PreDestroy指定销毁方法的Bean
     * @return
     */
    @Bean
    public PostConstructAndPreDestoryBean postConstructAndPreDestoryBean() {
        return new PostConstructAndPreDestoryBean();
    }


    /**
     * 注入AutowiredBean，名称为autowiredBean2，并将该bean作为默认依赖注入的首选
     * @return
     */
    @Bean
    @Primary
    public AutowiredBean autowiredBean2() {
        return new AutowiredBean();
    }
}