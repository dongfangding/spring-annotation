package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/8/8
 */
public class Log4jBean implements Slf4jBean {

    @Override
    public void info(String str) {
        System.out.println(this.getClass().getName() + ": " + str);
    }
}

