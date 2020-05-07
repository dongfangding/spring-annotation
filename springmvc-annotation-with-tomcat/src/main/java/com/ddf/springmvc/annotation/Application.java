package com.ddf.springmvc.annotation;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * @author DDf on 2018/8/19
 *
 */
public class Application {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(2181);
        tomcat.addWebapp("/mvc", "d:/workSpace/spring-annotation-demo/springmvc-annotation-with-tomcat/ ");
        tomcat.setHostname("localhost");
        tomcat.start();
        tomcat.getServer().await();
        System.out.println("------------------------");
    }
}
