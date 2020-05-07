package com.ddf.spring.annotation.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author DDf on 2018/7/31
 * 测试使用@Import注解结合ImportBeanDefinitionRegistrar
 */
public class CustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        // 做个简单的判断，如果标注了@Import注解的类上还有指定的另外一个注解，则导入一些组件
        if (annotationTypes.contains("org.springframework.context.annotation.Import")) {
            // 通过BeanDefinitionRegistry导入一个组件
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(
                    "com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean");
            // 通过BeanDefinitionRegistry导入的组件可以自定义bean的名称
            beanDefinitionRegistry.registerBeanDefinition("importBeanDefinitionRegistrarBean", rootBeanDefinition);
        }
    }
}
