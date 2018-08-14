package com.ddf.spring.annotation.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.spring.annotation.bean.DataSourceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author DDf on 2018/8/14
 * @Configuration 表名当时是一个配置类，如果自定义的扫描路径下包含了这个类，则该类会被自动识别成一个配置类
 * @EnableTransactionManagement 开启一个基于注解的事务,已标注在主配置类
 */
@Configuration
public class TransactionalConfiguration {
    /**
     * 向容器中注入DruidDataSource，属性来自于DataSourceConnection，
     * DataSourceConnection会自动从IOC容器中获取
     * 目前采用的main函数的写法，手动指定一个主配置类，因为不是web环境，当前配置类没有指定扫描包，而是在主配置类上指定的，
     * 所以当前类的这个方法的DataSourceConnection可能会提示没有这个bean，不用理会，如果是web环境就不会有问题了
     * @param dataSourceConnection
     * @return
     */
    @Bean
    public DruidDataSource druidDataSource(DataSourceConnection dataSourceConnection) {
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
    public JdbcTemplate jdbcTemplate(DruidDataSource druidDataSource) {
        return new JdbcTemplate(druidDataSource);
    }


    /**
     * 将数据源注入PlatformTransactionManager，这是一个接口，使用DataSourceTransactionManager的实现来管理事务
     * @param druidDataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }
}
