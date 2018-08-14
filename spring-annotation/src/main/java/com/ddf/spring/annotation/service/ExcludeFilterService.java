package com.ddf.spring.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/19
 * 该类的作用请参考{@link com.ddf.spring.annotation.configuration.ExcludeTypeFilter}是为了测试自定义规则决定是否导入某些bean
 */
@Service
public class ExcludeFilterService {
    public ExcludeFilterService() {
        System.out.println("ExcludeFilterService创建完成...............");
    }
}
