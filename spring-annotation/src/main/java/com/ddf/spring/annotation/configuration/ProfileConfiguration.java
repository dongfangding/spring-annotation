package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.Log4jBean;
import com.ddf.spring.annotation.bean.LogbackBean;
import com.ddf.spring.annotation.bean.Slf4jBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author DDf on 2018/8/8
 * 配置类2，因为一些特殊的测试需要重新启动IOC容器，在原来的测试类上有太多测试代码，重新启动后，有太多的打印影响观看，
 * 所以这里单独独立出来一个配置类，实际情况中一个配置类是完全可以的
 */
@Configuration
public class ProfileConfiguration {

    /**
     * 使用接口的形式根据环境注入接口的实现类
     * 如果当前环境是dev，Log4jBean
     * @return
     */
    @Bean
    @Profile("dev")
    public Slf4jBean log4jBean() {
        return new Log4jBean();
    }

    /**
     * 使用接口的形式根据环境注入接口的实现类
     * 如果当前环境是prd，则注入LogbackBean
     * @return
     */
    @Bean
    @Profile("prd")
    public Slf4jBean logbackBean() {
        return new LogbackBean();
    }
}
