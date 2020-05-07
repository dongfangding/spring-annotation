package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/7/31
 */
public class ImportSelectorBean {

    public ImportSelectorBean() {
        System.out.println("ImportSelectorBean创建完成，测试@Import通过" +
                "ImportSelector接口导入组件");
    }
}
