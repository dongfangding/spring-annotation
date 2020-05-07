package com.ddf.spring.annotation.configuration;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.time.LocalDateTime;

/**
 * @author DDf on 2018/7/21
 * 创建根据条件来判断是否导入某些组件，该类需要配合@Condition注解，@Condition注解需要用在要导入容器的地方，与导入组件注解组合使用，如果当前类
 * 返回true，则可以导入组件，反之，则不能。
 */
public class ProductionProfileCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取到bean定义的注册类(可以获取bean，注册bean，删除预定义的bean名称)
        BeanDefinitionRegistry registry = context.getRegistry();
        // 获取IOC容器使用的beanfactory（可以获取bean的定义信息，可以获取到bean的定义注册类）
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 可以获取到环境变量
        ConfigurableEnvironment environment = (ConfigurableEnvironment) context.getEnvironment();
        // 根据当前时间的分钟动态切换环境变量的值
        LocalDateTime localDateTime = LocalDateTime.now();
        int minute = localDateTime.getMinute();
        String profile;
        if (minute % 2 == 0) {
            profile = "dev";
        } else {
            profile = "prd";
        }
        System.out.println("ProductionProfileCondition profile: " + profile);
        // 如果是prd环境，并且当前IOC容器中未定义developmentService则返回true，同时注册一个ProductionServiceLog
        if ("prd".equals(profile)) {
            // 如果是prd环境，并且当前IOC容器中未定义developmentService则返回true，同时注册一个DevelopmentServiceLog
            if (!registry.containsBeanDefinition("developmentService")) {
                RootBeanDefinition prdServiceLogBean = new RootBeanDefinition(
                        "com.ddf.spring.annotation.bean.ProductionBeanLog");
                registry.registerBeanDefinition("prdServiceLog", prdServiceLogBean);
                return true;
            }
        }
        return false;
    }
}
