package com.ddf.spring.annotation.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author DDf on 2018/7/30
 * 测试和@Import一起使用通过ImportSelector来导入组件
 */
public class CustomImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Set<String> annotationTypes = importingClassMetadata.getAnnotationTypes();
        // 做个简单的判断，如果标注了@Import注解的类上还有指定的另外一个注解，则导入一些组件,不可以自定义bean的名称
        if (annotationTypes.contains("org.springframework.context.annotation.Import")) {
            return new String[]{"com.ddf.spring.annotation.bean.ImportSelectorBean"};
        }
        return new String[0];
    }
}
