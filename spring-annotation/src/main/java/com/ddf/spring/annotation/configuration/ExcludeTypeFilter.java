package com.ddf.spring.annotation.configuration;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 添加一个过滤bean导入的规则类
 *
 * @author DDf on 2018/7/19
 */
public class ExcludeTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 获取当前类资源（类的路径）
        Resource resource = metadataReader.getResource();

        String className = classMetadata.getClassName();
        System.out.println("自定义扫描类规则当前扫描类为： " + className);
        // 这一块是return true 还是return false是有讲究的，比如把这个规则当做excludeFilters，那么返回true，则匹配的会过滤掉,如果把这个规则
        // 应用到includeFilters,如果返回true，则是会加入到容器中
        if (className.contains("ExcludeFilter")) {
            return true;
        }
        return false;
    }
}
