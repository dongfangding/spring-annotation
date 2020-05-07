package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/8/8
 * 测试根据不同的profile来动态切换注册不同的类，该类为接口，使用接口接收参数，实际注入值为接口实现类
 */
public interface Slf4jBean {
    void info(String str);
}
