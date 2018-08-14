package com.ddf.spring.annotation.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/7/19
 * 使用@PropertySource引入外部配置文件
 * @Component或@Configuration将该类交由IOC容器保管（这里有一个很奇怪的地方，明明使用了@Bean在配置类中已经注入了这个Bean,但是如果
 * 这里只使用了@PropertySource依然无法对User赋值，所以这里需要再加上一个@Component，很奇怪）
 * 配置类@Bean参见{@link com.ddf.spring.annotation.configuration.AnnotationConfiguration#user()}
 */
@PropertySource("classpath:User.properties")
@Component
public class User {
    @Value("${user.id}")
    private Integer id;
    @Value("${user.userName}")
    private String userName;
    @Value("${user.password}")
    private String password;
    @Value("${user.tel}")
    private String tel;
    @Value("用户数据")
    private String defaultMessage;

    public void init() {
        System.out.println("User创建后调用初始化方法..........");
    }

    public void destory() {
        System.out.println("User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......");
    }

    public User() {
        System.out.println("User创建完成...通过@Bean的initMethod调用初始化方法............");
    }

    public User(Integer id, String userName, String password, String tel, String defaultMessage) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.tel = tel;
        this.defaultMessage = defaultMessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                ", defaultMessage='" + defaultMessage + '\'' +
                '}';
    }
}
