package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/2
 * BeanPostProcessor接口为bean的后置处理器，用来在bean的初始化前后做一些工作，需要将该类加入到容器中。
 * 需要理解的是，这个会在每个bean的生命周期内都会生效
 * postProcessBeforeInitialization()方法是在bean的初始化方法之前调用
 * postProcessAfterInitialization()方法是在bean的初始化方法之前调用之后执行
 *
 * 原理：
 * 遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization，
 *  * 一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization
 *  *
 *  * BeanPostProcessor原理
 *  * populateBean(beanName, mbd, instanceWrapper);给bean进行属性赋值
 *  * initializeBean
 *  * {
 *  * applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 *  * invokeInitMethods(beanName, wrappedBean, mbd);执行自定义初始化
 *  * applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *  *}
 *
 *  Spring底层对 BeanPostProcessor 的使用；
 *  * 		bean赋值，注入其他组件，@Autowired，生命周期注解功能，@Async,xxx BeanPostProcessor;
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在每个bean创建之后的初始化方法之前调用
     * @param bean 当前实例化的bean
     * @param beanName bean的名称
     * @return 返回实例化的bean或者可以对对象进行再封装返回
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【" + bean + "】");

        // 可以修改bean的预定义属性的值
        if (bean instanceof User) {
            User user = (User) bean;
            user.setUserName("用户名的值被CustomBeanPostProcessor改变了");
        }
        return bean;
    }

    /**
     * 在每个bean的初始化方法执行之后被调用
     * @param bean 当前实例化的bean
     * @param beanName bean的名称
     * @return 返回实例化的bean或者可以对对象进行再封装返回
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【" + bean + "】");
        return bean;
    }
}
