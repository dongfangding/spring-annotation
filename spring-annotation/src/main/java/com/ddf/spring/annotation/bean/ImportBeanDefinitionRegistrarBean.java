package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/7/31
 */
public class ImportBeanDefinitionRegistrarBean {

    public ImportBeanDefinitionRegistrarBean() {
        System.out.println("ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar" +
                "接口注入组件。。。。。");
    }
}
