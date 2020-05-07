package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.FactorySingletonBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author DDf on 2018/8/1
 * 通过FactoryBean工厂注册组件，该类本身需要注册到IOC容器中
 * 实际在IOC中注册的组件为FactoryBean中接口的方法来决定
 */
// @Component
public class FactorySingletonBeanConfiguration implements FactoryBean<FactorySingletonBean> {
    /**
     * 要注册的组件
     * @return
     */
    @Override
    public FactorySingletonBean getObject() {
        return new FactorySingletonBean();
    }

    /**
     * 要注册的组件类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return FactorySingletonBean.class;
    }

    /**
     * 要注册的组件是否是单实例
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}