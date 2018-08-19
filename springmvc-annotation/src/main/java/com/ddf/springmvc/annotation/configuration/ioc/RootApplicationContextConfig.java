package com.ddf.springmvc.annotation.configuration.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.springmvc.annotation.configuration.exception.GlobalExceptionHandler;
import com.ddf.springmvc.annotation.configuration.jdbc.DataSourceConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author DDf on 2018/8/16
 * 指定Spring根容器的配置类，排除web相关的bean
 *
 * @EnableTransactionManagement 开启基于注解的事务支持
 *
 */
@ComponentScan(value = "com.ddf.springmvc.annotation",
        excludeFilters = {@ComponentScan.Filter(value = Controller.class)})
@EnableTransactionManagement
public class RootApplicationContextConfig {

    public RootApplicationContextConfig() {
        System.out.println("RootApplicationContextConfig配置类被读取。。。。。。。。。。。。。。");
    }

    /**
     * 注册解析国际化资源文件的ResourceBundleMessageSource
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        // 指定资源文件，可以多个使用setBasenames指定多个，从classpath下读取
        resourceBundleMessageSource.setBasename("/exception/exception");
        System.out.println("注册MessageSource： " + resourceBundleMessageSource);
        return resourceBundleMessageSource;
    }

    /**
     * 将处理全局异常的Bean容器管理
     * @return
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 用户解析和格式化JSON的对象，在这里全局注册一个，用的时候直接注入即可。
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * 向容器中注入DataSource，属性来自于DataSourceConnection，实际注入的是DataSource的DruidDataSource实现
     * DataSourceConnection会自动从IOC容器中获取
     * @Primary 如果要注入通过类型获取的Bean类型为DataSource， 则默认获取druidDataSource。防止注入多个不同名DataSource其它使用地方报错
     * @param dataSourceConnection
     * @return
     */
    @Bean
    @Primary
    public DataSource druidDataSource(DataSourceConnection dataSourceConnection) {
        System.out.println(dataSourceConnection);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(dataSourceConnection.getName());
        druidDataSource.setUsername(dataSourceConnection.getUserName());
        druidDataSource.setPassword(dataSourceConnection.getPassword());
        druidDataSource.setUrl(dataSourceConnection.getUrl());
        // druidDataSource可以不指定driverClassName，会自动根据url识别
        druidDataSource.setDriverClassName(dataSourceConnection.getDriverClassName());
        return druidDataSource;
    }

    /**
     * 将数据源注入JdbcTemplate，再将JdbcTemplate注入到容器中，使用JdbcTemplate来操作数据库
     * @param druidDataSource
     * @return
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource druidDataSource) {
        return new JdbcTemplate(druidDataSource);
    }


    /**
     * 将数据源注入PlatformTransactionManager，这是一个接口，使用DataSourceTransactionManager的实现来管理事务
     * @param druidDataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }
}
