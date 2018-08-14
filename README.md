[TOC]

##一、容器
1. AnnotationConfigApplicationContex
2. [Bean的导入](https://gitee.com/dingdongfang/spring-annotation/tree/master#2-bean%E7%9A%84%E5%AF%BC%E5%85%A5-1)
3. Bean的生命周期
4. Bean的属性赋值
5. Bean的依赖注入
6. @Profile根据环境注入Bean


Spring注解驱动开发
项目准备
为了减少`jar`包的冗余，所以没有使用`Spring Initializr`来生成项目，直接新建一个`maven`项目,`pom.xml`如下
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ddf.spring</groupId>
    <artifactId>spring-annotation</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
    </properties>

    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.12.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>4.3.12.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.12.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/javax.inject/javax.inject -->
        <dependency>
            <groupId>javax.inject</groupId>
            <artifactId>javax.inject</artifactId>
            <version>1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.44</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>

    </dependencies>

</project>
```
## 一、容器
### 1. AnnotationConfigApplicationContext
#### 1.1 @Configuration配置类
@Configuration 该注解标注在某个类上，则可以被`spring`识别为一个配置类，配置类的作用等同于以前配置文件版的`xml`.具体需要在配置类中配置什么属性或起到什么作用，这就是另外注解的支持了，@Configuration只是保证你可以在该类中配置的功能能够被解析,如同一个空的`xxx.xml`配置文件,这是一切功能型注解生效的前提！
#### 1.2 @Bean注解注入`bean`
`@Bean`该注解可以标注在某个方法上，它实现的意义是为`IOC`中容器注入`bean`，此时`bean`的名称为方法名称，`bean`的类型为方法返回类型.以下为向容器中注入一个`type`为`User`的`bean`，该`bean`的名称为`user`,默认都是单实例`bean`
```java
@Bean
public User user() {
    return new User();
}
```
等同于

```xml
<bean id="user" class="xxx.xxx.User"/>
```
#### 1.3 创建AnnotationConfigApplicationContext

* 创建一个`Bean`对象`User.java`
```java
package com.ddf.spring.annotation.entity;

/**
 * @author DDf on 2018/7/19
 */
public class User {

    private Integer id;
    private String userName;
    private String password;
    private String tel;

    public User() {
        System.out.println("User创建完成...............");
    }

    public User(Integer id, String userName, String password, String tel) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.tel = tel;
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
}
```
* 新建一个配置类`AnnotationConfiguration`
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DDf on 2018/7/19
 */
@Configuration
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * @return
     */
    @Bean
    public User user() {
        return new User();
    }
}

```

* 新建主启动类`Application`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------");
        // 获取当前IOC中所有bean的名称
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }
    }
}

```
打印如下,可以看到除了最后一行我们注入的`User`以外，容器启动后会默认注入一些`bean`，但这些`bean`默认都是懒加载的，通过打印可以看出来，这是在容器已经初始化完成之后，获取预定义`bean`的名称然后调用`getBean()`方法之后获取才创建的，懒加载就是针对单实例的而且是在第一次过去才会被创建，而默认情况下都是容器启动后就会完成创建
```txt
-----------------------IOC容器初始化-------------------------
User创建完成...............
-----------------------IOC容器初始化完成-------------------------
bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$1906ac13
bean name:user, type: class com.ddf.spring.annotation.entity.User
```

### 2. Bean的导入
#### 2.1 @ComponentScan包扫描
该注解需要配合`@Configuration`标识的配置类上使用才会生效，它的作用是指定`spring`的扫描包，则在扫描路径下的被特定注解标识的类就会被自动纳入到`IOC`中，如我们熟知的`@Repository`、`@Service`、`@Controller`等，而不需要每个都使用`@Bean`注解一个个注入。
*属性详解
value
指定要扫描的包
excludeFilters 
指定扫描的时候按照什么规则排除那些组件
includeFilters 
指定扫描的时候只需要包含哪些组件，如果需要生效需要加入useDefaultFilters = false属性
FilterType.ANNOTATION
按照注解
FilterType.ASSIGNABLE_TYPE
按照给定的类
FilterType.ASPECTJ
使用ASPECTJ表达式
FilterType.REGEX
使用正则指定
FilterType.CUSTOM	使用自定义规则

* 添加一个`UserController.java`来测试排除注解类型为`@Controller`的类到容器中
```java
package com.ddf.spring.annotation.controller;

import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * 该类的作用是为了测试排除所有@Controller注解的类不需要注册到IOC容器中，
 * 详见{@link com.ddf.spring.annotation.configuration.AnnotationConfiguration}
 */
@Controller
public class UserController {
    public UserController() {
        System.out.println("UserController创建完成.................");
    }
}

```
* 添加一个`UserSerivce`，使用`@Service`注解标注来测试扫描注入
```java
package com.ddf.spring.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/19
 * 默认@Scope为singleton，IOC容器启动会创建该bean的实例，并且以后再次使用不会重新创建新的实例
 */
@Service
public class UserService {
    public UserService() {
        System.out.println("UserService创建完成...................");
    }
}

```

* 添加一个实现了`TypeFilter`接口的类，来自定义过滤规则`ExcludeTypeFilter.java`
```java
package com.ddf.spring.annotation.configuration;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
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
        System.out.println("自定义扫描类规则当前扫描类为： "+ className);
        // 这一块是return true 还是return false是有讲究的，比如把这个规则当做excludeFilters，那么返回true，则匹配的会过滤掉,如果把这个规则
        // 应用到includeFilters,如果返回true，则是会加入到容器中
        if (className.contains("ExcludeFilter")) {
            return true;
        }
        return false;
    }
}

```

* 添加一个符合`ExcludeTypeFilter.java`规则的`@Service`类，这个类本身应该会被纳入到容器中，在下面会演示如何根据自定义规则不纳入到容器中
```java
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

```

修改`AnnotationConfiguration.java`
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.service.IncludeFilterService;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type=FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * @return
     */
    @Bean
    public User user() {
        return new User();
    }
}

```
运行主启动类`Application.java`，可以看到多注入了名称为`userService`的`bean`，标注了`@Controller`的`UserController`被排除纳入，同样满足`ExcludeTypeFilter.java`定义规则的标注了`@Service`的`ExcludeFilterService.java`也被排除在外
```txt
-----------------------IOC容器初始化-------------------------
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
七月 19, 2018 11:53:53 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
User创建完成...............
-----------------------IOC容器初始化完成-------------------------
bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$19c06e9b
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:user, type: class com.ddf.spring.annotation.entity.User
```

#### 2.2 @Scope Bean的作用域
`Spring`初始化容器组件默认的`scope`都是`singleton`,即单实例。在容器初始化的时候就会把所有单实例的`bean`初始化创建出来，以后每次在使用的时候不会重新创建一个对象，可以通过`@Scope`来修改默认的作用域，作用域有以下属性
singleton	默认属性，单实例
prototype	启动创建，每次使用都是创建一个新的实例
request	基于`HttpServletRequest`，每次请求创建一个实例
session	基于`HttpSession`,每个`session`作用域下使用同一个实例

* 新建一个`Service`使用`@Scope("prototype")`注解修饰， `PrototypeScopeService.java`
```java
package com.ddf.spring.annotation.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/21
 * 测试@Scope的作用域为prototype，每次使用该bean都会重新生成一个实例
 */
@Service
@Scope("prototype")
public class PrototypeScopeService {
    public PrototypeScopeService() {
        System.out.println("PrototypeScopeService创建完成。。。。。。。。");
    }
}
```
* 使用上面建立的`UserService.java`来作为对比测试
```java
package com.ddf.spring.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/19
 * 默认@Scope为singleton，IOC容器启动会创建该bean的实例，并且以后再次使用不会重新创建新的实例
 */
@Service
public class UserService {
    public UserService() {
        System.out.println("UserService创建完成...................");
    }
}
```

* 修改主启动类`Application.java`，添加测试`@Scope`的方法`testPrototypeScopeService`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.PrototypeScopeService;
import com.ddf.spring.annotation.service.UserService;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------");
        // 获取当前IOC中所有bean的名称
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);

    }


    /**
     * 测试@Scope bean的作用域
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("-----------------------测试@Scope-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
    }
}
```
* 测试结果如控制台打印所示
```txt
-----------------------IOC容器初始化-------------------------
七月 21, 2018 6:52:36 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Sat Jul 21 18:52:36 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
七月 21, 2018 6:52:37 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
User创建完成...............
-----------------------IOC容器初始化完成-------------------------
bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$5ee69aa7
PrototypeScopeService创建完成。。。。。。。。
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:user, type: class com.ddf.spring.annotation.entity.User

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------
```

#### 2.2 @Lazy 单实例Bean的懒加载
默认情况下所有的单实例bean都会在容器启动的时候创建出来，使用的时候不会直接获取不会重复创建，但是可以通过`@Lazy`注解来让容器启动的时候不创建某些类，而是等待第一次获取使用该类的时候才创建该Bean对象.
* 创建`LazyBeanService.java`，使用`@Lazy`和`@Service`注解修饰来懒加载注入
```java
package com.ddf.spring.annotation.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/21
 * 测试单实例bean的@Lazy IOC容器启动的时候不创建该bean，只有到第一次获取的时候才创建
 * 但当第一次使用这个bean的时候再创建，以后使用不再创建
 */
@Service
@Lazy
public class LazyBeanService {
    public LazyBeanService() {
        System.out.println("LazyBeanService创建完成...............");
    }
}
```

* 修改主启动类`Application.java`，增加测试懒加载的方法`testLazyBeanService`,需要注意的是主启动类在以前的代码中为了测试方便，在启动后获取了所有预定义的`bean`的名称然后获取打印了出来，为了更清楚的测试懒加载这一块的`getBean()`需要注释掉，因为这里会牵扯到获取创建的问题
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.LazyBeanService;
import com.ddf.spring.annotation.service.PrototypeScopeService;
import com.ddf.spring.annotation.service.UserService;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        /*for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }*/

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);

    }


    /**
     * 测试@Scope bean的作用域
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("-----------------------测试@Scope-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("---------------测试单实例bean的@Lazy懒加载----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
    }
}
```

* 启动后查看控制台日志,`LazyBeanService`直到第一次获取的时候才被创建，并且是单实例
```txt
-----------------------IOC容器初始化-------------------------
七月 21, 2018 9:29:55 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Sat Jul 21 21:29:55 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
七月 21, 2018 9:29:55 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
User创建完成...............
-----------------------IOC容器初始化完成-------------------------

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------

Process finished with exit code 0
```

#### 2.2 @Conditional 动态判断是否导入Bean
`@Conditional`注解可以和导入`Bean`的注解组合使用，导入`Bean`的注解的作用是为容器中添加一个`Bean`，而且只要使用了注解就无条件的导入，而`@Conditional`注解可以指定一个类，该类必须实现`org.springframework.context.annotation.Condition`接口，重写`matches`方法返回true，则可以正常导入组件，但是如果返回false，则不能导入组件。而且该`Condition`类中还可以额外注册一些组件，这对于某些组件是否导入是根据某些条件导入的，而且导入之后还需要附加某些组件来说是很有用处的。
> 现在来模拟一个场景，有两个需要动态导入的`Bean`，一个是`DevelopmentBean`，该`Bean`有一个附加`Bean`为`DevelopmentBeanLog`，同时还有一个`Bean`名为`ProductionBean`，该`Bean`同样也有一个附加`Bean`名为`ProductionBeanLog`。现在的需求是根据不同的环境，如果是`dev`环境则注册的`DevelopmentBean`会生效，同时导入`DevelopmentBeanLog`。如果当前环境是`prd`，则注册的`ProductionBean`会生效，同时会导入`ProductionBeanLog`。环境的切换应该由外部配置文件来生效，但是为了方便测试，也为了不改代码就能够测试到两种不同的环境，所以使用当前时间的分钟，如果使偶数，则`dev`环境生效，如果使奇数，则`prd`环境生效;
* 创建`DevelopmentBean.java`
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/21
 */
public class DevelopmentBean {
    public DevelopmentBean() {
        System.out.println("DevelopmentBean创建完成.............");
    }
}
```

* 创建`DevelopmentBeanLog.java`
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/21
 */
public class DevelopmentBeanLog {
    public DevelopmentBeanLog() {
        System.out.println("DevelopmentBeanLog创建完成...............");
    }
}
```

* 创建`ProductionBean.java`
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/21
 */
public class ProductionBean {
    public ProductionBean() {
        System.out.println("ProductionBean创建完成.............");
    }
}
```

* 创建`ProductionBeanLog.java`
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/21
 */
public class ProductionBeanLog {
    public ProductionBeanLog() {
        System.out.println("ProductionBeanLog创建完成...................");
    }
}
```

* 创建`dev`环境生效的判断条件和导入组件类`DevelopmentProfileCondition.java`
```java
package com.ddf.spring.annotation.configuration;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.time.LocalDateTime;

/**
 * @author DDf on 2018/7/21
 * 创建根据条件来判断是否导入某些组件，该类需要配合@Condition注解，@Condition注解需要用在要导入容器的地方，与导入组件注解组合使用，如果当前类
 * 返回true，则可以导入组件，反之，则不能。
 */
public class DevelopmentProfileCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取到bean定义的注册类(可以获取bean，注册bean，删除预定义的bean名称)
        BeanDefinitionRegistry registry = context.getRegistry();
        // 获取IOC容器使用的beanfactory（可以获取bean的定义信息，可以获取到bean的定义注册类）
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 可以获取到环境变量
        ConfigurableEnvironment environment = (ConfigurableEnvironment) context.getEnvironment();
        // 根据当前时间的分钟动态切换环境变量的值
        LocalDateTime localDateTime = LocalDateTime.now();
        int minute = localDateTime.getMinute();
        String profile;
        if (minute % 2 == 0) {
            profile = "dev";
        } else {
            profile = "prd";
        }
        System.out.println("DevelopmentProfileCondition profile: " + profile);
        // 如果是dev环境，并且当前IOC容器中未定义ProductionBean则返回true，同时注册一个DevelopmentBeanLog
        if ("dev".equals(profile)) {
            if (!registry.containsBeanDefinition("ProductionBean")) {
                RootBeanDefinition devServiceLogBean = new RootBeanDefinition(
                        "com.ddf.spring.annotation.bean.DevelopmentBeanLog");
                registry.registerBeanDefinition("DevelopmentBeanLog", devServiceLogBean);
                return true;
            }
        }
        return false;
    }
}
```
* 创建`prd`环境生效的判断条件和导入组件类`ProductionProfileCondition.java`
```java
package com.ddf.spring.annotation.configuration;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.time.LocalDateTime;

/**
 * @author DDf on 2018/7/21
 * 创建根据条件来判断是否导入某些组件，该类需要配合@Condition注解，@Condition注解需要用在要导入容器的地方，与导入组件注解组合使用，如果当前类
 * 返回true，则可以导入组件，反之，则不能。
 */
public class ProductionProfileCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取到bean定义的注册类(可以获取bean，注册bean，删除预定义的bean名称)
        BeanDefinitionRegistry registry = context.getRegistry();
        // 获取IOC容器使用的beanfactory（可以获取bean的定义信息，可以获取到bean的定义注册类）
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 可以获取到环境变量
        ConfigurableEnvironment environment = (ConfigurableEnvironment) context.getEnvironment();
        // 根据当前时间的分钟动态切换环境变量的值
        LocalDateTime localDateTime = LocalDateTime.now();
        int minute = localDateTime.getMinute();
        String profile;
        if (minute % 2 == 0) {
            profile = "dev";
        } else {
            profile = "prd";
        }
        System.out.println("ProductionProfileCondition profile: " + profile);
        // 如果是prd环境，并且当前IOC容器中未定义DevelopmentBean则返回true，同时注册一个ProductionBeanLog
        if ("prd".equals(profile)) {
            // 如果是prd环境，并且当前IOC容器中未定义DevelopmentBean则返回true，同时注册一个DevelopmentBeanLog
            if (!registry.containsBeanDefinition("DevelopmentBean")) {
                RootBeanDefinition prdServiceLogBean = new RootBeanDefinition(
                        "com.ddf.spring.annotation.bean.ProductionBeanLog");
                registry.registerBeanDefinition("prdServiceLog", prdServiceLogBean);
                return true;
            }
        }
        return false;
    }
}
```
* 修改主配置类`AnnotationConfiguration`，使用`@Bean`注解和`@Condition`注解分别标注对应的类
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.DevelopmentBean;
import com.ddf.spring.annotation.bean.ProductionBean;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type=FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * @return
     */
    @Bean
    public User user() {
        return new User();
    }


    /**
     * 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean DevelopmentBean() {
        System.out.println("-------------------------测试@Conditional------------------");
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean ProductionBean() {
        System.out.println("-------------------------测试@Conditional------------------");
        return new ProductionBean();
    }
}
```

* 运行主启动类`Application.java`，控制台效果如下，通过控制台可以看到,虽然在配置类中定义了`ProductionBean()`和`DevelopmentBean()`两个方法，但是根据当前生效的环境，最终只会有一个被成功注入，另外一个就被忽略了，而且也可以看到在`Condition`类中注册的类比方法体中注册的要早。
```txt
-----------------------IOC容器初始化-------------------------
七月 21, 2018 10:54:48 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Sat Jul 21 22:54:48 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
DevelopmentProfileCondition profile: dev
ProductionProfileCondition profile: dev
七月 21, 2018 10:54:48 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
User创建完成...............
DevelopmentBeanLog创建完成...............
-------------------------测试@Conditional------------------
DevelopmentBean创建完成.............
-----------------------IOC容器初始化完成-------------------------
bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$75d3a279
LazyBeanService创建完成...............
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:user, type: class com.ddf.spring.annotation.entity.User
bean name:DevelopmentBeanLog, type: class com.ddf.spring.annotation.bean.DevelopmentBeanLog
bean name:DevelopmentBean, type: class com.ddf.spring.annotation.bean.DevelopmentBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------

Process finished with exit code 0
```

#### 2.3 @Import导入组件
> 该注解需要标注在带有`@Configuration`的类上，@Import的`value`属性可以直接指定一个普通的`java`类，即可直接导入组件，也可以实现`ImportSelector`接口来导入一组组件，也可以实现`ImportBeanDefinitionRegistrar`接口来获得当前注解信息，通过`BeanDefinitionRegistry`来注册组件，这种方式可以指定导入的组件ID
##### 2.3.1 @Import
* 新建一个普通的类`ImportBean.java`，用来实验直接通过`@Import`导入
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/30
 */
public class ImportBean {
    public ImportBean() {
        System.out.println("ImportBean创建完成（测试@Import导入组件）..........");
    }
}
```

* `Configuration`类参考2.3.4

##### 2.3.2 `@Import + ImportSelector`
* 新建一个`CustomImportSelector.java`实现`ImportSelector`接口
```java
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
```

* `Configuration`类参考2.3.4

##### 2.3.3 `@Import + ImportBeanDefinitionRegistrar`
* 新建一个`CustomImportBeanDefinitionRegistrar.java`实现`ImportBeanDefinitionRegistrar`接口
```java
package com.ddf.spring.annotation.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author DDf on 2018/7/31
 * 测试使用@Import注解结合ImportBeanDefinitionRegistrar
 */
public class CustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        // 做个简单的判断，如果标注了@Import注解的类上还有指定的另外一个注解，则导入一些组件
        if (annotationTypes.contains("org.springframework.context.annotation.Import")) {
            // 通过BeanDefinitionRegistry导入一个组件
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(
                    "com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean");
            // 通过BeanDefinitionRegistry导入的组件可以自定义bean的名称
            beanDefinitionRegistry.registerBeanDefinition("importBeanDefinitionRegistrarBean", rootBeanDefinition);
        }
    }
}
```

* `Configuration`类参考2.3.4

##### 2.3.4 修改`AnnotationConfiguration.java`配置类，在前面的基础上加上`@Import`的注解
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.DevelopmentBean;
import com.ddf.spring.annotation.bean.ImportBean;
import com.ddf.spring.annotation.bean.ProductionBean;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     *
     * @return
     */
    @Bean
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean DevelopmentBean() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean ProductionBean() {
        return new ProductionBean();
    }
}
```

##### 2.3.5 演示结果
* 启动主类`Application.java`，打印如下,可以看到`ImportBean.java ImportSelectorBean ImportBeanDefinitionRegistrarBean`在IOC容器初始化的时候创建成功。 
```txt
-----------------------IOC容器初始化-------------------------
八月 01, 2018 5:43:40 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Wed Aug 01 17:43:40 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportBeanDefinitionRegistrar
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportSelector
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryPrototypeBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactorySingletonBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportSelectorBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
DevelopmentProfileCondition profile: prd
ProductionProfileCondition profile: prd
八月 01, 2018 5:43:40 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
ImportBean创建完成（测试@Import导入组件）..........
ImportSelectorBean创建完成，测试@Import通过ImportSelector接口导入组件
User创建完成...............
ProductionBeanLog创建完成.......测试@Condition............
ProductionBean创建完成......测试@Condition.......
ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar接口注入组件。。。。。
-----------------------IOC容器初始化完成-------------------------

bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$d7abd5ba
LazyBeanService创建完成...............
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:com.ddf.spring.annotation.bean.ImportBean, type: class com.ddf.spring.annotation.bean.ImportBean
bean name:com.ddf.spring.annotation.bean.ImportSelectorBean, type: class com.ddf.spring.annotation.bean.ImportSelectorBean
bean name:user, type: class com.ddf.spring.annotation.entity.User
bean name:prdServiceLog, type: class com.ddf.spring.annotation.bean.ProductionBeanLog
bean name:ProductionBean, type: class com.ddf.spring.annotation.bean.ProductionBean
bean name:importBeanDefinitionRegistrarBean, type: class com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------

Process finished with exit code 0
```
#### 2.4 通过`FactoryBean`接口注入组件
>  通过FactoryBean工厂注册组件，该类本身需要注册到IOC容器中，如在类上标注`@Component`等或者使用`@Bean`注解,实际在IOC中注册的组件为FactoryBean中接口的方法来决定而非`FactoryBean`类本身，通过这种方式创建的组件可以`isSingleton`方法来决定组件是否为单实例，如果为单实例则同时该`Bean`是以懒加载的方式注册的

* 创建`FactorySingletonBean.java`，用来演示单实例bean
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/7/31
 * 该类用于测试使用FactoryBean来注册单实例组件
 */
public class FactorySingletonBean {
    public FactorySingletonBean() {
        System.out.println("FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。");
    }
}
```

* 创建`FactorySingletonBean`的`FactoryBean`接口,`FactorySingletonBeanConfiguration.java`
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.FactorySingletonBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/1
 * 通过FactoryBean工厂注册组件，该类本身需要注册到IOC容器中
 * 实际在IOC中注册的组件为FactoryBean中接口的方法来决定
 */
// @Component
public class FactorySingletonBeanConfiguration implements FactoryBean<FactorySingletonBean> {
    /**
     * 要注册的组件
     * @return
     */
    @Override
    public FactorySingletonBean getObject() {
        return new FactorySingletonBean();
    }

    /**
     * 要注册的组件类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return FactorySingletonBean.class;
    }

    /**
     * 要注册的组件是否是单实例
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

* 创建`FactoryPrototypeBean.java`，用来演示非单实例`bean`
```java
package com.ddf.spring.annotation.service;

/**
 * @author DDf on 2018/8/1
 * 该类用于测试使用FactoryBean来注册每个请求重新创建组件
 */
public class FactoryPrototypeBean {
    public FactoryPrototypeBean() {
        System.out.println("FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。");
    }
}
```

* 创建`FactoryPrototypeBean`的`FactoryBean`接口`FactoryPrototypeBeanConfiguration.java`
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.FactoryPrototypeBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/1
 * 通过FactoryBean工厂注册组件，该类本身需要注册到IOC容器中
 * 实际在IOC中注册的组件为FactoryBean中接口的方法来决定
 */
// @Component
public class FactoryPrototypeBeanConfiguration implements FactoryBean<FactoryPrototypeBean> {

    /**
     * 要注册的组件
     * @return
     */
    @Override
    public FactoryPrototypeBean getObject() {
        return new FactoryPrototypeBean();
    }

    /**
     * 要注册的组件类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return FactoryPrototypeBean.class;
    }

    /**
     * 要注册的组件是否是单实例
     * @return
     */
    @Override
    public boolean isSingleton() {
        return false;
    }
}
```

* 修改`AnnotationConfiguration.java`，使用配置类的方式将`FactoryPrototypeBeanConfiguration`和`FactorySingletonBeanConfiguration`两个`FactoryBean`接口注册到容器中
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.DevelopmentBean;
import com.ddf.spring.annotation.bean.ImportBean;
import com.ddf.spring.annotation.bean.ProductionBean;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     *
     * @return
     */
    @Bean
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean DevelopmentBean() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean ProductionBean() {
        return new ProductionBean();
    }


    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactoryPrototypeBeanConfiguration factoryPrototypeBeanConfiguration() {
        return new FactoryPrototypeBeanConfiguration();
    }

    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactorySingletonBeanConfiguration factorySingletonBeanConfiguration() {
        return new FactorySingletonBeanConfiguration();
    }
}
```

* 修改主启动类`Application.java`，用来验证`FactoryBean`创建`bean`的作用域
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.*;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);

    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }
}
```

* 启动测试,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的,因为在`IOC`容器创建的时候并没有创建，但是在获得所有定义的bean打印的时候才创建
```txt
-----------------------IOC容器初始化-------------------------
八月 01, 2018 6:06:46 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Wed Aug 01 18:06:46 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportBeanDefinitionRegistrar
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportSelector
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryPrototypeBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactorySingletonBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportSelectorBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
DevelopmentProfileCondition profile: dev
ProductionProfileCondition profile: dev
八月 01, 2018 6:06:46 下午 org.springframework.beans.factory.support.DefaultListableBeanFactory registerBeanDefinition
信息: Overriding bean definition for bean 'factoryPrototypeBeanConfiguration' with a different definition: replacing [Generic bean: class [com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in file [D:\dev-tools\idea_root\spring-annotation\target\classes\com\ddf\spring\annotation\configuration\FactoryPrototypeBeanConfiguration.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=annotationConfiguration; factoryMethodName=factoryPrototypeBeanConfiguration; initMethodName=null; destroyMethodName=(inferred); defined in com.ddf.spring.annotation.configuration.AnnotationConfiguration]
八月 01, 2018 6:06:46 下午 org.springframework.beans.factory.support.DefaultListableBeanFactory registerBeanDefinition
信息: Overriding bean definition for bean 'factorySingletonBeanConfiguration' with a different definition: replacing [Generic bean: class [com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in file [D:\dev-tools\idea_root\spring-annotation\target\classes\com\ddf\spring\annotation\configuration\FactorySingletonBeanConfiguration.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=annotationConfiguration; factoryMethodName=factorySingletonBeanConfiguration; initMethodName=null; destroyMethodName=(inferred); defined in com.ddf.spring.annotation.configuration.AnnotationConfiguration]
八月 01, 2018 6:06:47 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
ImportBean创建完成（测试@Import导入组件）..........
ImportSelectorBean创建完成，测试@Import通过ImportSelector接口导入组件
User创建完成...............
DevelopmentBeanLog创建完成.......测试@Condition........
DevelopmentBean创建完成.....测试@Condition........
ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar接口注入组件。。。。。
-----------------------IOC容器初始化完成-------------------------

bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$7e17d64c
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册单实例组件。。。。
bean name:factoryPrototypeBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactoryPrototypeBean
FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。
bean name:factorySingletonBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactorySingletonBean
LazyBeanService创建完成...............
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:com.ddf.spring.annotation.bean.ImportBean, type: class com.ddf.spring.annotation.bean.ImportBean
bean name:com.ddf.spring.annotation.bean.ImportSelectorBean, type: class com.ddf.spring.annotation.bean.ImportSelectorBean
bean name:user, type: class com.ddf.spring.annotation.entity.User
bean name:DevelopmentBeanLog, type: class com.ddf.spring.annotation.bean.DevelopmentBeanLog
bean name:DevelopmentBean, type: class com.ddf.spring.annotation.bean.DevelopmentBean
bean name:importBeanDefinitionRegistrarBean, type: class com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------


----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册单实例组件。。。。
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册单实例组件。。。。
单实例factorySingletonBean==factorySingletonBean1?true
Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?false
----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------


Process finished with exit code 0
```

### 3. `Bean`的生命周期
>  bean的生命周期指的是bean创建---初始化----销毁的过程
我们可以自定义初始化和销毁方法；容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
构造（对象创建）
单实例：在容器启动的时候创建对象
多实例：容器不会管理这个bean；容器不会调用销毁方法；

#### 3.1 `@Bean`指定初始化方法和销毁方法
* 修改之前创建的`User.java`，增加初始化和销毁方法，方法体与方法名称自定义即可
```java
package com.ddf.spring.annotation.entity;

/**
 * @author DDf on 2018/7/19
 */
public class User {

    private Integer id;
    private String userName;
    private String password;
    private String tel;

    public void init() {
        System.out.println("User创建后调用初始化方法..........");
    }

    public void destory() {
        System.out.println("User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......");
    }

    public User() {
        System.out.println("User创建完成...通过@Bean的initMethod调用初始化方法............");
    }

    public User(Integer id, String userName, String password, String tel) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.tel = tel;
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
}
```
* 修改主配置类`AnnotationConfiguration.java`,使用`@Bean`注册`User`的时候指定初始化和销毁方法
> initMethod 指定Bean创建后调用的初始化方法
destroyMethod 指定Bean在销毁后会调用的方法

```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.service.*;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * initMethod 指定Bean创建后调用的初始化方法
     * destroyMethod 指定Bean在销毁后会调用的方法
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destory")
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean DevelopmentBean() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean ProductionBean() {
        return new ProductionBean();
    }


    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactoryPrototypeBeanConfiguration factoryPrototypeBeanConfiguration() {
        return new FactoryPrototypeBeanConfiguration();
    }

    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactorySingletonBeanConfiguration factorySingletonBeanConfiguration() {
        return new FactorySingletonBeanConfiguration();
    }
}
```

#### 3.2 `InitializingBean`和`DisposableBean`接口
> 将要创建指定初始化和销毁方法的类实现初始化接口`InitializingBean`，如果需要指定销毁方法实现`DisposableBean`接口

* 创建一个实现了`InitializingBean`和`DisposableBean`接口的`Bean`
```java
package com.ddf.spring.annotation.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author DDf on 2018/8/1
 * 实现InitializingBean接口的afterPropertiesSet方法在Bean被创建后会自动调用该初始化方法
 * 实现DisposableBean接口的destroy方法会在容器会Bean被销毁时时调用该方法
 */
public class InitAndDisposableBean implements InitializingBean, DisposableBean {
    public InitAndDisposableBean() {
        System.out.println("InitAndDisposableBean创建完成。。。。。。。。。。。。");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitAndDisposableBean创建后实现InitializingBean调用初始化方法。。。。。。。。。。。。");
    }
}
```

#### 3.3 `@PostConstruct`和`@PreDestroy`注解
> 在需要指定初始化和销毁方法的Bean里创建对应的初始化和销毁方法，使用对应注解标注即可。
使用@PostConstruct指定Bean的初始化方法
使用@PreDestroy指定Bean销毁后调用的方法

* 创建`PostConstructAndPreDestoryBean.java`
```java
package com.ddf.spring.annotation.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author DDf on 2018/8/1
 * 使用JSR250注解实现Bean的初始化方法和销毁方法调用
 */
public class PostConstructAndPreDestoryBean {
    public PostConstructAndPreDestoryBean() {
        System.out.println("PostConstructAndPreDestoryBean创建完成.......");
    }

    /**
     * 使用@PostConstruct指定Bean的初始化方法
     */
    @PostConstruct
    public void init() {
        System.out.println("PostConstructAndPreDestoryBean创建完成，使用@PostConstruct注解来调用初始化方法。。。。");
    }


    /**
     * 使用@PreDestroy指定Bean销毁后调用的方法
     */
    @PreDestroy
    public void destroy() {
        System.out.println("PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。");
    }
}
```

#### 3.4 修改主启动类`Application.java`
以上三种形式，现在统一修改主启动类，然后集中验证,修改主启动类，在主程序最后关闭`IOC`容器
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.*;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }
}
```
#### 3.5 验证结果
运行主启动类，打印如下，可以看到容器在IOC容器创建后同时调用了初始化方法，并且在IOC容器关闭的时候调用了对应`Bean`的销毁方法
```
-----------------------IOC容器初始化-------------------------
八月 01, 2018 11:08:03 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Wed Aug 01 23:08:03 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportBeanDefinitionRegistrar
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportSelector
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryPrototypeBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactorySingletonBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportSelectorBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.InitAndDisposableBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
DevelopmentProfileCondition profile: dev
ProductionProfileCondition profile: dev
八月 01, 2018 11:08:04 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
UserService创建完成...................
ImportBean创建完成（测试@Import导入组件）..........
ImportSelectorBean创建完成，测试@Import通过ImportSelector接口导入组件
User创建完成...通过@Bean的initMethod调用初始化方法............
User创建后调用初始化方法..........
DevelopmentBeanLog创建完成.......测试@Condition........
DevelopmentBean创建完成.....测试@Condition........
InitAndDisposableBean创建完成。。。。。。。。。。。。
InitAndDisposableBean创建后实现InitializingBean调用初始化方法。。。。。。。。。。。。
PostConstructAndPreDestoryBean创建完成.......
PostConstructAndPreDestoryBean创建完成，使用@PostConstruct注解来调用初始化方法。。。。
ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar接口注入组件。。。。。
-----------------------IOC容器初始化完成-------------------------

bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$7081dc62
LazyBeanService创建完成...............
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:com.ddf.spring.annotation.bean.ImportBean, type: class com.ddf.spring.annotation.bean.ImportBean
bean name:com.ddf.spring.annotation.bean.ImportSelectorBean, type: class com.ddf.spring.annotation.bean.ImportSelectorBean
bean name:user, type: class com.ddf.spring.annotation.entity.User
bean name:DevelopmentBeanLog, type: class com.ddf.spring.annotation.bean.DevelopmentBeanLog
bean name:DevelopmentBean, type: class com.ddf.spring.annotation.bean.DevelopmentBean
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
bean name:factoryPrototypeBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactoryPrototypeBean
FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。
bean name:factorySingletonBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactorySingletonBean
bean name:initAndDisposableBean, type: class com.ddf.spring.annotation.bean.InitAndDisposableBean
bean name:postConstructAndPreDestoryBean, type: class com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
bean name:importBeanDefinitionRegistrarBean, type: class com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService创建完成。。。。。。。。
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------


----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
单实例factorySingletonBean==factorySingletonBean1?true
Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?false
----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------

八月 01, 2018 11:08:04 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
信息: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Wed Aug 01 23:08:03 CST 2018]; root of context hierarchy
PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。
InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........
User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......

Process finished with exit code 0
```

#### 3.6 BeanPostProcessor接口
> 参见`org.springframework.beans.factory.config.BeanPostProcessor`，该接口可以在每个`bean`的初始化方法调用之前和初始化方法调用之后执行指定的方法，
`postProcessBeforeInitialization()`在每个bean创建之后的初始化方法之前调用， `postProcessAfterInitialization`在每个bean的初始化方法执行之后被调用,执行实际千万和前面提到的初始化方法和销毁方法区分开。该方法通常用户修改预定义的`bean`的属性值，可以实现该接口进行覆盖。更详细参见章节`扩展原理之1. BeanFactoryPostProcessor接口`

* 新建`CustomBeanPostProcessor.java`实现`BeanPostProcessor`接口
```java
package com.ddf.spring.annotation.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/2
 * BeanPostProcessor接口为bean的后置处理器，用来在bean的初始化前后做一些工作，需要将该类加入到容器中。
 * 需要理解的是，这个会在每个bean的生命周期内都会生效
 * postProcessBeforeInitialization()方法是在bean的初始化方法之前调用
 * postProcessAfterInitialization()方法是在bean的初始化方法之前调用之后执行
 *
 * 原理：
 * 遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization，
 * * 一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization
 * *
 * * BeanPostProcessor原理
 * * populateBean(beanName, mbd, instanceWrapper);给bean进行属性赋值
 * * initializeBean
 * * {
 * * applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 * * invokeInitMethods(beanName, wrappedBean, mbd);执行自定义初始化
 * * applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 * *}
 *
 * Spring底层对 BeanPostProcessor 的使用；
 * * bean赋值，注入其他组件，@Autowired，生命周期注解功能，@Async,xxx BeanPostProcessor;
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在每个bean创建之后的初始化方法之前调用
     * @param bean 当前实例化的bean
     * @param beanName bean的名称
     * @return 返回实例化的bean或者可以对对象进行再封装返回
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【" + bean + "】");
        return bean;
    }


    /**
     * 在每个bean的初始化方法执行之后被调用
     * @param bean 当前实例化的bean
     * @param beanName bean的名称
     * @return 返回实例化的bean或者可以对对象进行再封装返回
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【" + bean + "】");
        return bean;
    }
}
```
* 运行主启动类`Application.java`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.*;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }
}
```

* 控制台打印如下，仔细观察`BeanPostProcessor`接口方法的执行时机，是在每个`bean`的初始化方法之前和之后两个地方呗调用
```java
-----------------------IOC容器初始化-------------------------
八月 02, 2018 10:21:08 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Thu Aug 02 22:21:08 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ApplicationContextUtil
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomBeanPostProcessor
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportBeanDefinitionRegistrar
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportSelector
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryPrototypeBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactorySingletonBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportSelectorBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.InitAndDisposableBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
DevelopmentProfileCondition profile: prd
ProductionProfileCondition profile: prd
八月 02, 2018 10:21:08 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【org.springframework.context.event.EventListenerMethodProcessor@77be656f】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【org.springframework.context.event.EventListenerMethodProcessor@77be656f】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【org.springframework.context.event.DefaultEventListenerFactory@221af3c0】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【org.springframework.context.event.DefaultEventListenerFactory@221af3c0】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$a202c61@23a5fd2】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$a202c61@23a5fd2】
UserService创建完成...................
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.UserService@78a2da20】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.UserService@78a2da20】
ImportBean创建完成（测试@Import导入组件）..........
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBean@dd3b207】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBean@dd3b207】
ImportSelectorBean创建完成，测试@Import通过ImportSelector接口导入组件
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportSelectorBean@551bdc27】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportSelectorBean@551bdc27】
User创建完成...通过@Bean的initMethod调用初始化方法............
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.entity.User@564718df】
User创建后调用初始化方法..........
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.entity.User@564718df】
ProductionBeanLog创建完成.......测试@Condition............
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ProductionBeanLog@18a70f16】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ProductionBeanLog@18a70f16】
ProductionBean创建完成......测试@Condition.......
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ProductionBean@62e136d3】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ProductionBean@62e136d3】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration@4206a205】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration@4206a205】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration@c540f5a】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration@c540f5a】
InitAndDisposableBean创建完成。。。。。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.InitAndDisposableBean@4d826d77】
InitAndDisposableBean创建后实现InitializingBean调用初始化方法。。。。。。。。。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.InitAndDisposableBean@4d826d77】
PostConstructAndPreDestoryBean创建完成.......
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean@44a664f2】
PostConstructAndPreDestoryBean创建完成，使用@PostConstruct注解来调用初始化方法。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean@44a664f2】
ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar接口注入组件。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean@7f9fcf7f】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean@7f9fcf7f】
-----------------------IOC容器初始化完成-------------------------

bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$a202c61
bean name:customBeanPostProcessor, type: class com.ddf.spring.annotation.configuration.CustomBeanPostProcessor
LazyBeanService创建完成...............
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.LazyBeanService@2c34f934】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.LazyBeanService@2c34f934】
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@12d3a4e9】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@12d3a4e9】
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:com.ddf.spring.annotation.bean.ImportBean, type: class com.ddf.spring.annotation.bean.ImportBean
bean name:com.ddf.spring.annotation.bean.ImportSelectorBean, type: class com.ddf.spring.annotation.bean.ImportSelectorBean
bean name:user, type: class com.ddf.spring.annotation.entity.User
bean name:prdServiceLog, type: class com.ddf.spring.annotation.bean.ProductionBeanLog
bean name:ProductionBean, type: class com.ddf.spring.annotation.bean.ProductionBean
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@240237d2】
bean name:factoryPrototypeBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactoryPrototypeBean
FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactorySingletonBean@25a65b77】
bean name:factorySingletonBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactorySingletonBean
bean name:initAndDisposableBean, type: class com.ddf.spring.annotation.bean.InitAndDisposableBean
bean name:postConstructAndPreDestoryBean, type: class com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
bean name:importBeanDefinitionRegistrarBean, type: class com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@548a102f】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@548a102f】
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@5762806e】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@5762806e】
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------


----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@17c386de】
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@5af97850】
单实例factorySingletonBean==factorySingletonBean1?true
Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?false
----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------

PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。
InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........
User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......
八月 02, 2018 10:21:08 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
信息: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Thu Aug 02 22:21:08 CST 2018]; root of context hierarchy

Process finished with exit code 0
```
### 4. `Bean`的属性赋值
#### 4.1 `@Value`与`@PropertySource`
> `@Value`可以标注在类的字段上，以表示该该字段赋值，可以直接使用目标值，支持`SPEl`，也支持外部配置文件加载
`@PropertySource`可以标注在类上，`value`可以指定一个资源文件，则可以导入该文件以便解析读取

* 在`src/main/resources`下新建一个配置文件`User.properties`，用来给`User`初始化赋值
```properties
user.id=1
user.userName=ddf
user.password=123456
user.tel=18356785555
```

* 修改`User.java`，引入配置文件和给属性赋值
```java
package com.ddf.spring.annotation.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/7/19
 * 使用@PropertySource引入外部配置文件
 * @Component或@Configuration将该类交由IOC容器保管（这里有一个很奇怪的地方，明明使用了@Bean在配置类中已经注入了这个Bean,但是如果
 * 这里只使用了@PropertySource依然无法对User赋值，所以这里需要再加上一个@Component，很奇怪）
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
```
* 修改`Application.java`，增加测试代码`testPropertySourceValue`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.*;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);
        // 测试@PropertySource和@Value属性赋值
        testPropertySourceValue(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }

    /**
     * 测试通过@PropertySource和@Value注解来对属性进行赋值
     * @param applicationContext
     */
    public static void testPropertySourceValue(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试@PropertySource和@Value赋值开始----------------");
        User user = applicationContext.getBean(User.class);
        System.out.println("user属性为： " + user.toString());
        System.out.println("---------------测试@PropertySource和@Value赋值结束----------------\n");

    }
}
```

* 运行类，日志如下
```txt
-----------------------IOC容器初始化-------------------------
八月 04, 2018 7:14:19 下午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5197848c: startup date [Sat Aug 04 19:14:19 CST 2018]; root of context hierarchy
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.Application
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ApplicationContextUtil
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomBeanPostProcessor
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportBeanDefinitionRegistrar
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.CustomImportSelector
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.DevelopmentProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ExcludeTypeFilter
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.configuration.ProductionProfileCondition
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.controller.UserController
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.entity.User
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.DevelopmentBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.ExcludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactoryPrototypeBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.FactorySingletonBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ImportSelectorBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.IncludeFilterService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.InitAndDisposableBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.LazyBeanService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBean
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.bean.ProductionBeanLog
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.PrototypeScopeService
自定义扫描类规则当前扫描类为： com.ddf.spring.annotation.service.UserService
八月 04, 2018 7:14:19 下午 org.springframework.beans.factory.support.DefaultListableBeanFactory registerBeanDefinition
信息: Overriding bean definition for bean 'user' with a different definition: replacing [Generic bean: class [com.ddf.spring.annotation.entity.User]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in file [J:\dev-tools\idea-root\spring-annotation\target\classes\com\ddf\spring\annotation\entity\User.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=annotationConfiguration; factoryMethodName=user; initMethodName=init; destroyMethodName=destory; defined in com.ddf.spring.annotation.configuration.AnnotationConfiguration]
DevelopmentProfileCondition profile: dev
ProductionProfileCondition profile: dev
八月 04, 2018 7:14:19 下午 org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
信息: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【org.springframework.context.event.EventListenerMethodProcessor@b7f23d9】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【org.springframework.context.event.EventListenerMethodProcessor@b7f23d9】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【org.springframework.context.event.DefaultEventListenerFactory@69b794e2】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【org.springframework.context.event.DefaultEventListenerFactory@69b794e2】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$c59c92dd@4d339552】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$c59c92dd@4d339552】
解析的字符串：你好 Windows 10 我是 360
com.ddf.spring.annotation.configuration.ApplicationContextUtil@45018215
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.ApplicationContextUtil@45018215】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.ApplicationContextUtil@45018215】
User创建完成...通过@Bean的initMethod调用初始化方法............
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【User{id=1, userName='ddf', password='123456', tel='18356785555', defaultMessage='用户数据'}】
User创建后调用初始化方法..........
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【User{id=1, userName='ddf', password='123456', tel='18356785555', defaultMessage='用户数据'}】
UserService创建完成...................
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.UserService@47d90b9e】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.UserService@47d90b9e】
ImportBean创建完成（测试@Import导入组件）..........
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBean@1184ab05】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBean@1184ab05】
ImportSelectorBean创建完成，测试@Import通过ImportSelector接口导入组件
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportSelectorBean@3aefe5e5】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportSelectorBean@3aefe5e5】
DevelopmentBeanLog创建完成.......测试@Condition........
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.DevelopmentBeanLog@149e0f5d】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.DevelopmentBeanLog@149e0f5d】
DevelopmentBean创建完成.....测试@Condition........
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.DevelopmentBean@1b1473ab】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.DevelopmentBean@1b1473ab】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration@ef9296d】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactoryPrototypeBeanConfiguration@ef9296d】
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration@1c93084c】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.configuration.FactorySingletonBeanConfiguration@1c93084c】
InitAndDisposableBean创建完成。。。。。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.InitAndDisposableBean@7ce3cb8e】
InitAndDisposableBean创建后实现InitializingBean调用初始化方法。。。。。。。。。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.InitAndDisposableBean@7ce3cb8e】
PostConstructAndPreDestoryBean创建完成.......
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean@22a637e7】
PostConstructAndPreDestoryBean创建完成，使用@PostConstruct注解来调用初始化方法。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean@22a637e7】
ImportBeanDefinitionRegistrarBean创建完成，测试@Import接口通过ImportBeanDefinitionRegistrar接口注入组件。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean@6fe7aac8】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean@6fe7aac8】
-----------------------IOC容器初始化完成-------------------------

bean name:org.springframework.context.annotation.internalConfigurationAnnotationProcessor, type: class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean name:org.springframework.context.annotation.internalAutowiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalRequiredAnnotationProcessor, type: class org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
bean name:org.springframework.context.annotation.internalCommonAnnotationProcessor, type: class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean name:org.springframework.context.event.internalEventListenerProcessor, type: class org.springframework.context.event.EventListenerMethodProcessor
bean name:org.springframework.context.event.internalEventListenerFactory, type: class org.springframework.context.event.DefaultEventListenerFactory
bean name:annotationConfiguration, type: class com.ddf.spring.annotation.configuration.AnnotationConfiguration$$EnhancerBySpringCGLIB$$c59c92dd
bean name:applicationContextUtil, type: class com.ddf.spring.annotation.configuration.ApplicationContextUtil
bean name:customBeanPostProcessor, type: class com.ddf.spring.annotation.configuration.CustomBeanPostProcessor
bean name:user, type: class com.ddf.spring.annotation.entity.User
LazyBeanService创建完成...............
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.LazyBeanService@1700915】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.LazyBeanService@1700915】
bean name:lazyBeanService, type: class com.ddf.spring.annotation.service.LazyBeanService
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@21de60b4】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@21de60b4】
bean name:prototypeScopeService, type: class com.ddf.spring.annotation.service.PrototypeScopeService
bean name:userService, type: class com.ddf.spring.annotation.service.UserService
bean name:com.ddf.spring.annotation.bean.ImportBean, type: class com.ddf.spring.annotation.bean.ImportBean
bean name:com.ddf.spring.annotation.bean.ImportSelectorBean, type: class com.ddf.spring.annotation.bean.ImportSelectorBean
bean name:DevelopmentBeanLog, type: class com.ddf.spring.annotation.bean.DevelopmentBeanLog
bean name:DevelopmentBean, type: class com.ddf.spring.annotation.bean.DevelopmentBean
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@c267ef4】
bean name:factoryPrototypeBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactoryPrototypeBean
FactorySingletonBean创建完成。。。。，测试通过FactoryBean来注册单实例组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactorySingletonBean@30ee2816】
bean name:factorySingletonBeanConfiguration, type: class com.ddf.spring.annotation.bean.FactorySingletonBean
bean name:initAndDisposableBean, type: class com.ddf.spring.annotation.bean.InitAndDisposableBean
bean name:postConstructAndPreDestoryBean, type: class com.ddf.spring.annotation.bean.PostConstructAndPreDestoryBean
bean name:importBeanDefinitionRegistrarBean, type: class com.ddf.spring.annotation.bean.ImportBeanDefinitionRegistrarBean

-----------------------测试@Scope开始-------------------------
默认单实例bean UserService是否相等 true
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@7a69b07】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@7a69b07】
PrototypeScopeService创建完成。。。。。。。。
BeanPostProcessor的postProcessBeforeInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@5e82df6a】
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.service.PrototypeScopeService@5e82df6a】
PrototypeScopeService prototype scope作用域是否相等: false
-----------------------测试@Scope结束-------------------------


---------------测试单实例bean的@Lazy懒加载开始----------------------
lazyBeanService==lazyBeanService1？: true
---------------测试单实例bean的@Lazy懒加载结束----------------------


----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@3f197a46】
FactoryPrototypeBean创建完成....，测试通过FactoryBean来注册Prototype组件。。。。
BeanPostProcessor的postProcessAfterInitialization方法执行，当前bean【com.ddf.spring.annotation.bean.FactoryPrototypeBean@636be97c】
单实例factorySingletonBean==factorySingletonBean1?true
Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?false
----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------


---------------测试@PropertySource和@Value赋值开始----------------
user属性为： User{id=1, userName='ddf', password='123456', tel='18356785555', defaultMessage='用户数据'}
---------------测试@PropertySource和@Value赋值结束----------------

PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。
InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........
User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......
```

### 5. `Bean`的依赖注入
> 如何将一个组件自动注入到另外一个组件中呢？
#### 5.1 `@Autowired`
> `@Autowired`可以可以标注在构造器，方法，参数，字段上，默认按照`Bean`的类型去`IOC`容器中去寻找组件，如果有且一个组件被找到则成功注入，如果有多个组件被找到，则再使用默认名称去获取`Bean`,`Bean`的默认名称为类首字母缩写。也可以配合`@Qualifier`注解明确指定注入哪个`Bean`，还可以在注册`Bean`的地方使用`@Primary`，在不指定`@Qualifier`的情况下，默认注入@Primary修饰的Bean。如果`Bean`不一定存在，可以使用属性`required=false`，则`Bean`不存在也不会抛出异常

* 编写一个用来注入到另外一个类中的`Bean AutowiredBean.java`并使用`@Component`注入到容器中
```java
package com.ddf.spring.annotation.bean;

import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/6
 */
@Component
public class AutowiredBean {
    public AutowiredBean() {
        System.out.println("AutowiredBean创建完成。。。。。。。。。。。。");
    }
}
```

* 修改`AnnotationConfiguration.java`，增加方法`autowiredBean2()`,再次注入`AutowiredBean `，并取名`autowiredBean2`区分
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.*;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * initMethod 指定Bean创建后调用的初始化方法
     * destroyMethod 指定Bean在销毁后会调用的方法
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destory")
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean developmentService() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean productionService() {
        return new ProductionBean();
    }


    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactoryPrototypeBeanConfiguration factoryPrototypeBeanConfiguration() {
        return new FactoryPrototypeBeanConfiguration();
    }

    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactorySingletonBeanConfiguration factorySingletonBeanConfiguration() {
        return new FactorySingletonBeanConfiguration();
    }


    /**
     * 注册一个实现InitializingBean, DisposableBean接口来指定Bean的初始化和销毁方法的Bean
     * @return
     */
    @Bean
    public InitAndDisposableBean initAndDisposableBean() {
        return new InitAndDisposableBean();
    }

    /**
     * 创建一个通过JSR250 @PostConstruct指定初始化方法/@PreDestroy指定销毁方法的Bean
     * @return
     */
    @Bean
    public PostConstructAndPreDestoryBean postConstructAndPreDestoryBean() {
        return new PostConstructAndPreDestoryBean();
    }


    /**
     * 注入AutowiredBean，名称为autowiredBean2，并将该bean作为默认依赖注入的首选
     * @return
     */
    @Bean
    @Primary
    public AutowiredBean autowiredBean2() {
        return new AutowiredBean();
    }
}
```

* 新建一个`AutowiredService.java`并使用`@Service`注解交由容器管理，然后注入`AutowiredBean`
```java
package com.ddf.spring.annotation.service;

import com.ddf.spring.annotation.bean.AutowiredBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/8/6
 * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
 * * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
 * * 默认注入@Primary修饰的Bean，如果Bean不一定存在，可以使用属性required=false，则Bean不存在也不会抛出异常
 * 可以标注在构造器，方法，参数，字段上
 *
 * @Resource 默认使用名称注入Bean,可以使用name属性指定具体要注入的Bean的名称，不支持@Primary，不支持required=false,不支持@Primary
 * @Inject 使用类型去注入，支持@Primary，不支持required=false
 *
 *
 */
@Service
public class AutowiredService {
    @Autowired
    private AutowiredBean autowiredBean;
    public AutowiredBean getAutowiredBean() {
        return autowiredBean;
    }

    @Autowired
    @Qualifier("autowiredBean")
    private AutowiredBean qualifierAutowiredBean;
    public AutowiredBean getQualifierAutowiredBean() {
        return qualifierAutowiredBean;
    }

    public AutowiredService() {
        System.out.println("AutowiredService创建完成。。。。。。。。。。。。");
    }
}
```

* 测试见 5.3 综合演示测试结果


#### 5.2 `@Resource`和`@Inject`
> 这两个注解也可以完成依赖注入，并且是`java`规范提供的。区别为`@Resource`默认是使用名称来注入组件的，并且不支持`@Primary`，也不支持`required=false`，有一个属性名为name可以指定要注入的Bean的名称；`@Inject`和`@Autowired`类似，按照类型注入，支持`@Primary`，不支持`required=false`

* 修改`AutowiredService.java`,加入`@Resource`和`@Inject`的注入代码
```java
package com.ddf.spring.annotation.service;

import com.ddf.spring.annotation.bean.AutowiredBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author DDf on 2018/8/6
 * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
 * * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
 * * 默认注入@Primary修饰的Bean，如果Bean不一定存在，可以使用属性required=false，则Bean不存在也不会抛出异常
 * 可以标注在构造器，方法，参数，字段上
 *
 * @Resource 默认使用名称注入Bean,可以使用name属性指定具体要注入的Bean的名称，不支持@Primary，不支持required=false,不支持@Primary
 * @Inject 使用类型去注入，支持@Primary，不支持required=false
 *
 *
 */
@Service
public class AutowiredService {
    @Autowired
    private AutowiredBean autowiredBean;
    public AutowiredBean getAutowiredBean() {
        return autowiredBean;
    }

    @Autowired
    @Qualifier("autowiredBean")
    private AutowiredBean qualifierAutowiredBean;
    public AutowiredBean getQualifierAutowiredBean() {
        return qualifierAutowiredBean;
    }

    @Resource(name = "autowiredBean")
    private AutowiredBean resourceAutowiredBean;
    public AutowiredBean getResourceAutowiredBean() {
        return resourceAutowiredBean;
    }


    @Resource(name = "autowiredBean2")
    private AutowiredBean resourceAutowiredBean2;
    public AutowiredBean getResourceAutowiredBean2() {
        return resourceAutowiredBean2;
    }

    @Inject
    private UserService userService;
    public UserService getUserService() {
        return userService;
    }

    public AutowiredService() {
        System.out.println("AutowiredService创建完成。。。。。。。。。。。。");
    }
}
```

* 测试见 5.3 综合演示测试结果
#### 5.3 综合演示测试结果

* 修改主启动类`Application.java`，增加测试依赖注入的方法`testAutowired()`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.AutowiredService;
import com.ddf.spring.annotation.service.LazyBeanService;
import com.ddf.spring.annotation.service.PrototypeScopeService;
import com.ddf.spring.annotation.service.UserService;
import com.ddf.spring.annotation.bean.AutowiredBean;
import com.ddf.spring.annotation.bean.FactoryPrototypeBean;
import com.ddf.spring.annotation.bean.FactorySingletonBean;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }

        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);
        // 测试@PropertySource和@Value属性赋值
        testPropertySourceValue(applicationContext);
        // 测试@Autowired注入多个相同类型的Bean
        testAutowired(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }

    /**
     * 测试通过@PropertySource和@Value注解来对属性进行赋值
     * @param applicationContext
     */
    public static void testPropertySourceValue(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试@PropertySource和@Value赋值开始----------------");
        User user = applicationContext.getBean(User.class);
        System.out.println("user属性为： " + user.toString());
        System.out.println("---------------测试@PropertySource和@Value赋值结束----------------\n");

    }

    /**
     * 测试在IOC容器中存在两个相同类型的Bean,但是Bean的名称不一致
     * 在这种情况下，使用@Autowired将该Bean注入到另外一个容器中
     * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
     * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
     * 默认注入哪个Bean {@link AutowiredService}
     * @param applicationContext
     */
    public static void testAutowired(ApplicationContext applicationContext) {
        System.out.println("\n--------------测试autowired注入多个相同类型的类开始-----------------");
        AutowiredBean autowiredBean = (AutowiredBean) applicationContext.getBean("autowiredBean");
        AutowiredBean autowiredBean2 = (AutowiredBean) applicationContext.getBean("autowiredBean2");
        System.out.println("autowiredBean: " + autowiredBean);
        System.out.println("autowiredBean2: " + autowiredBean2);
        System.out.println(autowiredBean == autowiredBean2);

        /**
         * 这里已做更改，修改了默认注入 {@link com.ddf.spring.annotation.configuration.AnnotationConfiguration.autowiredBean2}
         */
        AutowiredService autowiredService = applicationContext.getBean(AutowiredService.class);
        AutowiredBean autowiredServiceBean = autowiredService.getAutowiredBean();
        System.out.println("使用@Primay后AutowiredService默认注入bean: " + autowiredServiceBean);

        AutowiredBean autowiredServiceBean2 = autowiredService.getQualifierAutowiredBean();
        System.out.println("使用@Qualifier明确注入Bean: " + autowiredServiceBean2);

        // 使用@Resource注入
        AutowiredBean resourceAutowiredBean = autowiredService.getResourceAutowiredBean();
        System.out.println("使用@Resource注入autowiredBean: " + resourceAutowiredBean);
        AutowiredBean resourceAutowiredBean2 = autowiredService.getResourceAutowiredBean2();
        System.out.println("使用@Resource注入autowiredBean2: " + resourceAutowiredBean2);

        // 使用@Inject注入
        UserService userService = autowiredService.getUserService();
        System.out.println("使用@Inject注入UserService： " + userService);

        System.out.println("--------------测试autowired注入多个相同类型的类开始-----------------\n");
    }
}
```

* 打印日志如下,不相关日志已过滤
```txt
// 过滤。。。
--------------测试autowired注入多个相同类型的类开始-----------------
autowiredBean: com.ddf.spring.annotation.bean.AutowiredBean@77e4c80f
autowiredBean2: com.ddf.spring.annotation.bean.AutowiredBean@1d119efb
false
使用@Primay后AutowiredService默认注入bean: com.ddf.spring.annotation.bean.AutowiredBean@1d119efb
使用@Qualifier明确注入Bean: com.ddf.spring.annotation.bean.AutowiredBean@77e4c80f
使用@Resource注入autowiredBean: com.ddf.spring.annotation.bean.AutowiredBean@77e4c80f
使用@Resource注入autowiredBean2: com.ddf.spring.annotation.bean.AutowiredBean@1d119efb
使用@Inject注入UserService： com.ddf.spring.annotation.service.UserService@4b5d6a01
--------------测试autowired注入多个相同类型的类开始-----------------
```

### 6. `@Profile`根据环境注入`Bean`
> `@Profile`注解，Spring为我们提供的可以根据当前环境，动态的激活和切换一系列组件的功能，加了环境标识的bean，只有这个环境被激活的时候才能注册到容器中。默认是default环境，写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效,没有标注环境标识的bean在，任何环境下都是加载的；

演示场景： 
> 加入有一个日志管理接口类，在这个接口下实现不同的具体日志实现方式，根据不同的环境来注入不同的日志实现类。如，当前有一个接口类`Slf4jBean`，有`Log4jBean`和`LogbackBean`两个实现，现决定如果使`dev`环境，则使用`Lo4jBean`来管理日志，如果`prd`环境，则使用`LogbackBean`;

* 新建日志接口类`Slf4jBean.java`
```java
package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/8/8
 * 测试根据不同的profile来动态切换注册不同的类，该类为接口，使用接口接收参数，实际注入值为接口实现类
 */
public interface Slf4jBean {
    void info(String str);
}
```

* 新建日志实现类`Log4jBean.java`
```java
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
```

* 新建另一个日志实现类`LogbackBean.java`
```java
package com.ddf.spring.annotation.bean;

/**
 * @author DDf on 2018/8/8
 */
public class LogbackBean implements Slf4jBean {
    @Override
    public void info(String str) {
        System.out.println(this.getClass().getName() + ": " + str);
    }
}
```

* 新建一个配置类`ProfileConfiguration.java`，使用`@Profile`注解在方法上根据不同环境注入不同的类
其实这一块完全可以使用之前一直使用的配置类`AnnotationConfiguration`，只不过切换环境牵扯到多次的重启`IOC`容器，而之前的测试大多都是在容器启动时测试的，因此有大量的打印在控制台上，如果继续使用这个配置类，会在大量的日志里看到当前的测试，会非常不易观察，因此这一块单独出来用一个新的配置类
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.Log4jBean;
import com.ddf.spring.annotation.bean.LogbackBean;
import com.ddf.spring.annotation.bean.Slf4jBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author DDf on 2018/8/8
 * 配置类2，因为一些特殊的测试需要重新启动IOC容器，在原来的测试类上有太多测试代码，重新启动后，有太多的打印影响观看，
 * 所以这里单独独立出来一个配置类，实际情况中一个配置类是完全可以的
 */
@Configuration
public class ProfileConfiguration {

    /**
     * 使用接口的形式根据环境注入接口的实现类
     * 如果当前环境是dev，Log4jBean
     * @return
     */
    @Bean
    @Profile("dev")
    public Slf4jBean log4jBean() {
        return new Log4jBean();
    }

    /**
     * 使用接口的形式根据环境注入接口的实现类
     * 如果当前环境是prd，则注入LogbackBean
     * @return
     */
    @Bean
    @Profile("prd")
    public Slf4jBean logbackBean() {
        return new LogbackBean();
    }
}
```

* 修改主启动类`Application.java`，增加测试`@profile`的方法`testProfile`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.service.AutowiredService;
import com.ddf.spring.annotation.service.LazyBeanService;
import com.ddf.spring.annotation.service.PrototypeScopeService;
import com.ddf.spring.annotation.service.UserService;
import com.ddf.spring.annotation.bean.AutowiredBean;
import com.ddf.spring.annotation.bean.FactoryPrototypeBean;
import com.ddf.spring.annotation.bean.FactorySingletonBean;
import com.ddf.spring.annotation.bean.Slf4jBean;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.ProfileConfiguration;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        printBeans(applicationContext);
        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);
        // 测试@PropertySource和@Value属性赋值
        testPropertySourceValue(applicationContext);
        // 测试@Autowired注入多个相同类型的Bean
        testAutowired(applicationContext);
        // 测试@Profile根据环境注入Bean
        testProfile(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 打印当前IOC容器中所有预定义的Bean
     * @param applicationContext
     */
    private static void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }

    /**
     * 测试通过@PropertySource和@Value注解来对属性进行赋值
     * @param applicationContext
     */
    public static void testPropertySourceValue(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试@PropertySource和@Value赋值开始----------------");
        User user = applicationContext.getBean(User.class);
        System.out.println("user属性为： " + user.toString());
        System.out.println("---------------测试@PropertySource和@Value赋值结束----------------\n");

    }

    /**
     * 测试在IOC容器中存在两个相同类型的Bean,但是Bean的名称不一致
     * 在这种情况下，使用@Autowired将该Bean注入到另外一个容器中
     * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
     * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
     * 默认注入哪个Bean {@link AutowiredService}
     * @param applicationContext
     */
    public static void testAutowired(ApplicationContext applicationContext) {
        System.out.println("\n--------------测试autowired注入多个相同类型的类开始-----------------");
        AutowiredBean autowiredBean = (AutowiredBean) applicationContext.getBean("autowiredBean");
        AutowiredBean autowiredBean2 = (AutowiredBean) applicationContext.getBean("autowiredBean2");
        System.out.println("autowiredBean: " + autowiredBean);
        System.out.println("autowiredBean2: " + autowiredBean2);
        System.out.println(autowiredBean == autowiredBean2);

        /**
         * 这里已做更改，修改了默认注入 {@link com.ddf.spring.annotation.configuration.AnnotationConfiguration.autowiredBean2}
         */
        AutowiredService autowiredService = applicationContext.getBean(AutowiredService.class);
        AutowiredBean autowiredServiceBean = autowiredService.getAutowiredBean();
        System.out.println("使用@Primay后AutowiredService默认注入bean: " + autowiredServiceBean);

        AutowiredBean autowiredServiceBean2 = autowiredService.getQualifierAutowiredBean();
        System.out.println("使用@Qualifier明确注入Bean: " + autowiredServiceBean2);

        // 使用@Resource注入
        AutowiredBean resourceAutowiredBean = autowiredService.getResourceAutowiredBean();
        System.out.println("使用@Resource注入autowiredBean: " + resourceAutowiredBean);
        AutowiredBean resourceAutowiredBean2 = autowiredService.getResourceAutowiredBean2();
        System.out.println("使用@Resource注入autowiredBean2: " + resourceAutowiredBean2);

        // 使用@Inject注入
        UserService userService = autowiredService.getUserService();
        System.out.println("使用@Inject注入UserService： " + userService);

        System.out.println("--------------测试autowired注入多个相同类型的类开始-----------------\n");
    }


    /**
     * 测试根据激活的Profile来根据环境注册不同的Bean
     * 切换profile有两种方式：
     * 1. 在java虚拟机启动参数加 -Dspring.profiles.active=test
     * 2. 如下演示，使用代码切换，可以将切换的变量放在配置文件，spring-boot配置文件即是这种方式
     * @param applicationContext
     */
    public static void testProfile(AnnotationConfigApplicationContext applicationContext) {
        System.out.println("------------------测试@Profile-------------------------");
        // 重新新建一个IOC容器
        applicationContext = new AnnotationConfigApplicationContext();
        // 注册配置类
        applicationContext.register(ProfileConfiguration.class);
        // 设置当前激活的环境profile
        applicationContext.getEnvironment().setActiveProfiles("dev");
        // 刷新容器
        applicationContext.refresh();

        // 使用接口获得实际接口实现类的注入Bean
        Slf4jBean devLogBean = applicationContext.getBean(Slf4jBean.class);
        devLogBean.info("测试环境");

        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileConfiguration.class);
        applicationContext.getEnvironment().setActiveProfiles("prd");
        applicationContext.refresh();

        Slf4jBean prdLogBean = applicationContext.getBean(Slf4jBean.class);
        prdLogBean.info("生产环境");

        System.out.println("------------------测试@Profile-------------------------");
    }
}
```

* 打印日志如下，可以看到首先使用的`dev`环境，然后打印的是`com.ddf.spring.annotation.bean.Log4jBean: 测试环境`，然后切换为`prd`环境，打印的是`com.ddf.spring.annotation.bean.LogbackBean: 生产环境`
```txt
.......省略其它不相关日志............
------------------测试@Profile-------------------------
PostConstructAndPreDestoryBean容器销毁，使用@PreDestroy注解来指定调用销毁方法。。。。
InitAndDisposableBean容器销毁，实现DisposableBean接口调用销毁方法...........
User销毁后调用销毁方法....通过@Bean的destoryMethod指定销毁方法......
com.ddf.spring.annotation.bean.Log4jBean: 测试环境
com.ddf.spring.annotation.bean.LogbackBean: 生产环境
------------------测试@Profile-------------------------
```

### 7. 扩展`Aware`接口
> 使用相关的Aware接口可以直接获取到Spring底层设计的容器相关的类和属性

```java
package com.ddf.spring.annotation.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @author DDf on 2018/8/2
 * 使用Aware可以获得的属性
 * ApplicationContextAware 可以获取到IOC容器
 * BeanNameAware 可以获取到当前bean的名称
 * EmbeddedValueResolverAware
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {
    private ApplicationContext applicationContext;
    private String beanName;

    /**
     * 获得当前的bean的名称
     * @param name
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * 获得当前的IOC容器
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println(this.applicationContext.getBean(beanName));
    }

    /**
     * 字符串解析器
     * @param resolver
     */
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String resolveStringValue = resolver.resolveStringValue("你好 ${os.name} 我是 #{20*18}");
        System.out.println("解析的字符串："+resolveStringValue);
    }
}
```

## 二、`AOP`
> 这一章节开始讲述有关`AOP`相关的注解内容，即面向切面变成，`AOP`可以很方便的在某个方法某个业务逻辑前后，无侵入式的加入逻辑代码或者日志或者权限等

### 1. `@Aspect`自定义切面
> 该注解可以用来标注一个类，则该类会被识别成一个`AOP`类，同时该类必须也需要交由容器管理，如使用`@Component`，切面包含了一系列的注解来定义切面语法如`@Pointcut`,
定义了切面方法的执行时机，如`@Before`

@EnableAspectJAutoProxy	开启基于注解的aop模式,如果使用aop则必须在配置类上加入这个注解
@Aspect	表明当前是一个切面类，需要配合@Component等类似功能注解，交由容器管理
@Pointcut	定义一个切面语法，该语法直接影响到当前类的实际作用范围
@Before	在目标方法执行之前执行
@After	在目标方法执行结束之后执行
@AfterReturning	在结果值返回之后，可以在这里获取到方法的返回值,如果在结果值返回之前出现i异常，则这个方法不会被执行
@AfterThrowing	如果方法出现异常，则执行该方法
@Around	环绕通知，在使用了以上注解，再使用这个之后，发现方法正常执行后，结果值不能正确获取，方法出现异常，依然能执行到@AfterReturning，而且控制台异常被屏蔽，这个不做详细演示

* 使用`@Aspect`定义一个切面类,并使用`@Component`将该类交由容器管理,定义切面语法
```java
package com.ddf.spring.annotation.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * @author DDf on 2018/8/10
 *
 * 标注一个AOP切面类
 * @Aspect 表明当前是一个切面类
 * @Component 必须将切面类交由容器管理，同时切面类也仅对同在容器中的类提供支持，仅仅满足切面语法不在类却不在容器中也是不行的，
 *
 * 还有一个很重要的地方，切面类的语法不能包含本切面类，如果本切面类也满足自己定义的切面语法，会出现问题
 *
 *
 */
@Aspect
public class LogAspect {

    /**
     * 使用@Pointcut来定义一个通用的切面表达式，方法体不会被执行，而别的方法切面类可以直接引用当前方法来复用表达式
     * 切面语法：
     * 首先是固定写法execution(访问修饰符 返回值 类路径 方法(参数1,参数2等))
     *
     * *的省略原则
     * 如果有一层是希望不加以限制的，则可以使用*代替，而如果连续的两个位置都不加以限制，则可以直接使用一个*号
     *
     * 包与子包
     * 如果类路径只限制到某个包下所有类则直接包名然后.*即可，但如果指定到某个包，某个包下面本身有类，而包下面又有子包，
     * 则可以指定到当前包之后使用两个.然后再*,如..*
     *
     * 参数的省略
     * 如果明确指定两个参数并且指定类型，则可以(string,string)
     * 但是如果一个参数固定，另外一个不加以限制，则可以(string,*)
     * 如果参数类型和数量都不加以限制，则可以(..)
     *
     * 如下示例，则拦截com.ddf.spring.annotation.service包以及所有子包下的所有public的方法（返回值不限，参数不限）
     *
     */
    @Pointcut("execution(public * com.ddf.spring.annotation.service..*.*(..))")
    public void pointcut() {};

    /**
     * 在目标方法执行之前执行
     * @param joinPoint
     */
    @Before("pointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + "开始执行,参数列表" + Arrays.asList(args));
    }


    /**
     * 在目标方法执行结束之后执行
     * @param joinPoint
     */
    @After("pointcut()")
    public void afterLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + "结束执行,参数列表" + Arrays.asList(args));
    }


    /**
     * 在结果值返回之后，可以在这里获取到方法的返回值,如果在结果值返回之前出现i异常，则这个方法不会被执行
     * @param joinPoint 这个参数要保证在第一位
     * @param result 返回结果
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + "结束执行,参数列表" + Arrays.asList(args) + "，返回结果: " + result);
    }


    /**
     * 如果方法出现异常，则执行该方法
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value="pointcut()",throwing="exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + "结束执行,参数列表" + Arrays.asList(args) + "，出现异常: " + exception);
    }
}
```

* 修改`UserService.java`，加入两个验证方法，一定要希望被拦截的目标类也必须被`Spring`管理
```java
package com.ddf.spring.annotation.service;

import org.springframework.stereotype.Service;

/**
 * @author DDf on 2018/7/19
 * 默认@Scope为singleton，IOC容器启动会创建该bean的实例，并且以后再次使用不会重新创建新的实例
 */
@Service
public class UserService {
    public UserService() {
        System.out.println("UserService创建完成...................");
    }

    public String welcome(String userName) {
        return "Hello " + userName;
    }

    public String welcomeException(String userName) {
        throw new RuntimeException("出现异常");
    }
}
```

* 修改主配置类`AnnotationConfiguration.java`，加入`@EnableAspectJAutoProxy`，开启基于注解的`AOP`代理
```java
package com.ddf.spring.annotation.configuration;

import com.ddf.spring.annotation.bean.*;
import com.ddf.spring.annotation.entity.User;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * @author DDf on 2018/7/19
 * @Configuration 表明当前类是一个配置类
 * @ComponentScan 指定扫描的包路径，并且配置了excludeFilters来排除注解类型为@Controller的不纳入容器中，
 * 排除符合自定义ExcludeTypeFilter类中规则的类
 * @Import 导入组件，可以直接导入普通类，或者通过ImportSelector接口或者ImportBeanDefinitionRegistrar接口来自定义导入
 *
 * @EnableAspectJAutoProxy 开启基于注解的aop模式
 */
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
@EnableAspectJAutoProxy
public class AnnotationConfiguration {

    /**
     * 注入一个Type为User（方法返回值）的bean，bean的名称为user（方法名）
     * initMethod 指定Bean创建后调用的初始化方法
     * destroyMethod 指定Bean在销毁后会调用的方法
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destory")
    public User user() {
        return new User();
    }


    /**
     * 测试@Conditional 满足{@link DevelopmentProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({DevelopmentProfileCondition.class})
    public DevelopmentBean developmentService() {
        return new DevelopmentBean();
    }


    /**
     * 满足{@link ProductionProfileCondition} 这个类的条件返回true则当前Bean能够成功注入，反之不能
     *
     * @return
     */
    @Bean
    @Conditional({ProductionProfileCondition.class})
    public ProductionBean productionService() {
        return new ProductionBean();
    }


    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactoryPrototypeBeanConfiguration factoryPrototypeBeanConfiguration() {
        return new FactoryPrototypeBeanConfiguration();
    }

    /**
     * 使用FactoryBean工厂来注册组件
     * @return
     */
    @Bean
    public FactorySingletonBeanConfiguration factorySingletonBeanConfiguration() {
        return new FactorySingletonBeanConfiguration();
    }


    /**
     * 注册一个实现InitializingBean, DisposableBean接口来指定Bean的初始化和销毁方法的Bean
     * @return
     */
    @Bean
    public InitAndDisposableBean initAndDisposableBean() {
        return new InitAndDisposableBean();
    }

    /**
     * 创建一个通过JSR250 @PostConstruct指定初始化方法/@PreDestroy指定销毁方法的Bean
     * @return
     */
    @Bean
    public PostConstructAndPreDestoryBean postConstructAndPreDestoryBean() {
        return new PostConstructAndPreDestoryBean();
    }

    /**
     * 注入AutowiredBean，名称为autowiredBean2，并将该bean作为默认依赖注入的首选
     * @return
     */
    @Bean
    @Primary
    public AutowiredBean autowiredBean2() {
        return new AutowiredBean();
    }
}
```
* 修改主启动类`Application.java`,增加测试方法`testAspect`
```java
package com.ddf.spring.annotation;

import com.ddf.spring.annotation.bean.AutowiredBean;
import com.ddf.spring.annotation.bean.FactoryPrototypeBean;
import com.ddf.spring.annotation.bean.FactorySingletonBean;
import com.ddf.spring.annotation.bean.Slf4jBean;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.ProfileConfiguration;
import com.ddf.spring.annotation.entity.User;
import com.ddf.spring.annotation.service.AutowiredService;
import com.ddf.spring.annotation.service.LazyBeanService;
import com.ddf.spring.annotation.service.PrototypeScopeService;
import com.ddf.spring.annotation.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        printBeans(applicationContext);
        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);
        // 测试@PropertySource和@Value属性赋值
        testPropertySourceValue(applicationContext);
        // 测试@Autowired注入多个相同类型的Bean
        testAutowired(applicationContext);
        // 测试@Profile根据环境注入Bean
        testProfile(applicationContext);
        // 测试AOP
        testAspect(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 打印当前IOC容器中所有预定义的Bean
     * @param applicationContext
     */
    private static void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }

    /**
     * 测试通过@PropertySource和@Value注解来对属性进行赋值
     * @param applicationContext
     */
    public static void testPropertySourceValue(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试@PropertySource和@Value赋值开始----------------");
        User user = applicationContext.getBean(User.class);
        System.out.println("user属性为： " + user.toString());
        System.out.println("---------------测试@PropertySource和@Value赋值结束----------------\n");

    }

    /**
     * 测试在IOC容器中存在两个相同类型的Bean,但是Bean的名称不一致
     * 在这种情况下，使用@Autowired将该Bean注入到另外一个容器中
     * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
     * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
     * 默认注入哪个Bean {@link AutowiredService}
     * @param applicationContext
     */
    public static void testAutowired(ApplicationContext applicationContext) {
        System.out.println("\n--------------测试autowired注入多个相同类型的类开始-----------------");
        AutowiredBean autowiredBean = (AutowiredBean) applicationContext.getBean("autowiredBean");
        AutowiredBean autowiredBean2 = (AutowiredBean) applicationContext.getBean("autowiredBean2");
        System.out.println("autowiredBean: " + autowiredBean);
        System.out.println("autowiredBean2: " + autowiredBean2);
        System.out.println(autowiredBean == autowiredBean2);

        /**
         * 这里已做更改，修改了默认注入 {@link com.ddf.spring.annotation.configuration.AnnotationConfiguration.autowiredBean2}
         */
        AutowiredService autowiredService = applicationContext.getBean(AutowiredService.class);
        AutowiredBean autowiredServiceBean = autowiredService.getAutowiredBean();
        System.out.println("使用@Primay后AutowiredService默认注入bean: " + autowiredServiceBean);

        AutowiredBean autowiredServiceBean2 = autowiredService.getQualifierAutowiredBean();
        System.out.println("使用@Qualifier明确注入Bean: " + autowiredServiceBean2);

        // 使用@Resource注入
        AutowiredBean resourceAutowiredBean = autowiredService.getResourceAutowiredBean();
        System.out.println("使用@Resource注入autowiredBean: " + resourceAutowiredBean);
        AutowiredBean resourceAutowiredBean2 = autowiredService.getResourceAutowiredBean2();
        System.out.println("使用@Resource注入autowiredBean2: " + resourceAutowiredBean2);

        // 使用@Inject注入
        UserService userService = autowiredService.getUserService();
        System.out.println("使用@Inject注入UserService： " + userService);

        System.out.println("--------------测试autowired注入多个相同类型的类结束-----------------\n");
    }


    /**
     * 测试根据激活的Profile来根据环境注册不同的Bean
     * 切换profile有两种方式：
     * 1. 在java虚拟机启动参数加 -Dspring.profiles.active=test
     * 2. 如下演示，使用代码切换，可以将切换的变量放在配置文件，spring-boot配置文件即是这种方式
     * @param applicationContext
     */
    public static void testProfile(AnnotationConfigApplicationContext applicationContext) {
        System.out.println("\n------------------测试@Profile开始-------------------------");
        // 重新新建一个IOC容器
        applicationContext = new AnnotationConfigApplicationContext();
        // 注册配置类
        applicationContext.register(ProfileConfiguration.class);
        // 设置当前激活的环境profile
        applicationContext.getEnvironment().setActiveProfiles("dev");
        // 刷新容器
        applicationContext.refresh();

        // 使用接口获得实际接口实现类的注入Bean
        Slf4jBean devLogBean = applicationContext.getBean(Slf4jBean.class);
        devLogBean.info("测试环境");

        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileConfiguration.class);
        applicationContext.getEnvironment().setActiveProfiles("prd");
        applicationContext.refresh();

        Slf4jBean prdLogBean = applicationContext.getBean(Slf4jBean.class);
        prdLogBean.info("生产环境");

        System.out.println("------------------测试@Profile结束-------------------------\n");
    }


    /**
     * 测试AOP
     * @param applicationContext
     */
    public static void testAspect(ApplicationContext applicationContext) {
        System.out.println("\n--------------------测试AOP开始----------------------------");

        UserService userService = applicationContext.getBean(UserService.class);
        userService.welcome("ddf");
        userService.welcomeException("ddf");

        System.out.println("--------------------测试AOP结束----------------------------\n");
    }
}
```

* 启动主启动类`Application.java`，查看控制台日志如下，可以看到,`UserService`的`welcome`方法顺利执行`@Before`、`@After`、`AfterReturning`方法，并且接收到了返回值，但是`welcomeException`方法出现了异常，只执行了`@Before`方法和`@After`方法，`AfterReturning`方法没有执行取而代之执行了`@AfterThrowing`方法
```txt
// 省略其它不相干日志
--------------------测试AOP开始----------------------------
welcome开始执行,参数列表[ddf]
welcome结束执行,参数列表[ddf]
welcome结束执行,参数列表[ddf]，返回结果: Hello ddf
welcomeException开始执行,参数列表[ddf]
welcomeException结束执行,参数列表[ddf]
welcomeException结束执行,参数列表[ddf]，出现异常: java.lang.RuntimeException: 出现异常
Exception in thread "main" java.lang.RuntimeException: 出现异常
 at com.ddf.spring.annotation.service.UserService.welcomeException(UserService.java:20)
 at com.ddf.spring.annotation.service.UserService$$FastClassBySpringCGLIB$$851f891e.invoke(<generated>)
 at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)
 at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:738)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)
 at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:52)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
 at org.springframework.aop.aspectj.AspectJAfterAdvice.invoke(AspectJAfterAdvice.java:47)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
 at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:52)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
 at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
 at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)
 at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
 at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:673)
 at com.ddf.spring.annotation.service.UserService$$EnhancerBySpringCGLIB$$7f50f9a0.welcomeException(<generated>)
 at com.ddf.spring.annotation.Application.testAspect(Application.java:206)
 at com.ddf.spring.annotation.Application.main(Application.java:41)

Process finished with exit code 1
```

### 2. 事务控制
本节演示如何使用注解版的事务，一般使用事务的前提为: 
1. 必须有数据源连接`DataSource`
2. 必须有事务管理器，并且将`DataSource`注入到事务管理器中

#### 2.1 配置数据源与事务支持
I. 使用`mysql`和`druid`来连接数据库,在之前的`pom.xml`中已经导入依赖
II. 创建数据库,sql需要分开执行，先建立数据库，然后进入到数据库在执行建表语句
```sql
CREATE DATABASE `spring-annotation` CHARACTER SET utf8 COLLATE utf8_general_ci;

USER `spring-annotation`
DROP TABLE IF EXISTS PERSON;
CREATE TABLE PERSON(
 ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
 NAME VARCHAR(64),
 BIRTH_DAY DATE,
 TEL VARCHAR(32),
  ADDRESS VARCHAR(200)
);
```


III. 配置数据源
I. 在`classpath`下新建一个数据库连接信息的配置文件`jdbc.properties`
```properties
jdbc.name=druid
jdbc.userName=root
jdbc.password=123456
jdbc.url=jdbc:mysql://localhost:3306/spring-annotation?characterEncoding=utf8&useSSL=true
jdbc.driverClassName=com.mysql.jdbc.Driver
```
II. 创建一个保存配置文件信息的类`DataSourceConnection.java`
```java
package com.ddf.spring.annotation.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author DDf on 2018/8/13
 */
@Component
@PropertySource("classpath:jdbc.properties")
public class DataSourceConnection {
    @Value("${jdbc.name}")
    private String name;
    @Value("${jdbc.userName}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DataSourceConnection{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
```
III.新建一个专门用户管理数据连接与数据相关的配置类，需要明确一点的是，配置类在同一个项目中可以存在多个，但是配置类必须被`@CompontScan`扫描到才可以，所以可以项目中有一个主配置类，其他的配置类只要标注`@Configuration`并且满足主配置类配置的`@CompontScan`扫描规则即可，，就会被自动识别.`@EnableTransactionManagement`这个注解标书在配置类上，则开启了基于注解版事务的支持，否则注解无效。这个注解最好放在主配置类上，这样更能方便的看到主配置类就能明确到当前项目开启了哪些功能注解，当然这个放在自己的配置类也是可以的。
主配置类加注解`@EnableTransactionManagement`，其余省略与前面不变
```java
@Configuration
@ComponentScan(value = "com.ddf.spring.annotation", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = ExcludeTypeFilter.class)
})
@Import(value = {ImportBean.class, CustomImportSelector.class, CustomImportBeanDefinitionRegistrar.class})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AnnotationConfiguration {
    //.............
}
```
新建配置类`TransactionalConfiguration.java`，注入数据库连接`DruidDataSource`，事务支持`PlatformTransactionManager`以及数据库操作简化支持`JdbcTemplate`
```java
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
```

#### 2.2 测试与总结
I. 新建一个`Person`类
```java
package com.ddf.spring.annotation.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author DDf on 2018/8/14
 */
public class Person implements RowMapper<Person> {
    private Integer id;
    private String name;
    private Date birthDay;
    private String address;
    private String tel;

    public Person() {
    }

    public Person(Integer id, String name, Date birthDay, String address, String tel) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.address = address;
        this.tel = tel;
    }

    public Person(String name, Date birthDay, String address, String tel) {
        this.name = name;
        this.birthDay = birthDay;
        this.address = address;
        this.tel = tel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setBirthDay(resultSet.getDate("birthDay"));
        person.setAddress(resultSet.getString("address"));
        person.setTel(resultSet.getString("tel"));
        return person;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDay=" + birthDay +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
```
II. 新建`PersonService`，增加添加person的方法，一个不使用事务，一个使用注解事务
```java
package com.ddf.spring.annotation.service;

import com.ddf.spring.annotation.Application;
import com.ddf.spring.annotation.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DDf on 2018/8/14
 */
@Service
public class PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 不使用事务的方法保存一个person
     * @param person
     * @return
     */
    public int add(Person person) {
        String sql = "INSERT INTO PERSON(NAME, BIRTH_DAY, TEL, ADDRESS) VALUES (?, ?, ?, ?)";
        int i = jdbcTemplate.update(sql, person.getName(), person.getBirthDay(), person.getTel(), person.getAddress());
        if (i == 1) {
            throw new RuntimeException("抛出异常");
        }
        return i;
    }


    /**
     * 使用事务控制来保存一个person，在保存结束后抛出异常，看是否会回滚
     * @param person
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int addWithTransactional(Person person) {
        String sql = "INSERT INTO PERSON(NAME, BIRTH_DAY, TEL, ADDRESS) VALUES (?, ?, ?, ?)";
        int i = jdbcTemplate.update(sql, person.getName(), person.getBirthDay(), person.getTel(), person.getAddress());
        if (i == 1) {
            throw new RuntimeException("抛出异常");
        }
        return i;
    }
}
```

III. 修改`Application.java`，测试数据库连接是否正常以及测试person添加
```java
   /**
     * 测试连接数据源
     */
    public static void testDruidDataSource(ApplicationContext applicationContext) {
        System.out.println("\n--------------------获取DruidDataSource数据库连接开始----------------------------");
        DataSourceConnection dataSourceConnection = applicationContext.getBean(DataSourceConnection.class);
        System.out.println(dataSourceConnection);
        DruidDataSource druidDataSource = applicationContext.getBean(DruidDataSource.class);
        DruidPooledConnection connection = null;
        try {
            connection = druidDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(connection);
        System.out.println("--------------------获取DruidDataSource数据库连接结束----------------------------\n");
    }


    /**
     * 测试增加一个Person
     * 使用事务和不使用事务对比
     * @param applicationContext
     */
    public static void testAddPerson(ApplicationContext applicationContext) {
        System.out.println("\n-------------------测试增加一个Person开始-------------------------");
        PersonService personService = applicationContext.getBean(PersonService.class);
        Calendar calendar = new GregorianCalendar();
        calendar.set(1992, 4, 29);
        personService.add(new Person("no_transactional", calendar.getTime(), "上海市", "18356789999"));

        personService.addWithTransactional(new Person("with_transactional", calendar.getTime(), "上海市", "18356789999"));

        System.out.println("-------------------测试增加一个Person结束-------------------------\n");
    }
```
完整版如下
```java
package com.ddf.spring.annotation;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ddf.spring.annotation.bean.*;
import com.ddf.spring.annotation.configuration.AnnotationConfiguration;
import com.ddf.spring.annotation.configuration.ProfileConfiguration;
import com.ddf.spring.annotation.entity.Person;
import com.ddf.spring.annotation.entity.User;
import com.ddf.spring.annotation.service.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author DDf on 2018/7/19
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("-----------------------IOC容器初始化-------------------------");
        // 创建一个基于配置类启动的IOC容器，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别，如果主配置类扫描包的路径下包含其他配置类，则其他配置类可以被自动识别
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        System.out.println("-----------------------IOC容器初始化完成-------------------------\n");
        // 获取当前IOC中所有bean的名称,即使是懒加载类型的bean也会获取到
        printBeans(applicationContext);
        // 测试@Scope bean的作用域
        testPrototypeScopeService(applicationContext);
        // 测试单实例bean的@Lazy懒加载
        testLazyBeanService(applicationContext);
        // 测试FactoryBean接口导入单实例与Prototype作用域的组件
        testFactoryBeanPrototypeBean(applicationContext);
        // 测试@PropertySource和@Value属性赋值
        testPropertySourceValue(applicationContext);
        // 测试@Autowired注入多个相同类型的Bean
        testAutowired(applicationContext);
        // 测试@Profile根据环境注入Bean
        testProfile(applicationContext);
        // 测试AOP
        testAspect(applicationContext);
        // 测试获取数据库连接
        testDruidDataSource(applicationContext);
        // 测试事务
        testAddPerson(applicationContext);

        // 销毁容器
        applicationContext.close();
    }


    /**
     * 打印当前IOC容器中所有预定义的Bean
     * @param applicationContext
     */
    private static void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        // 打印当前IOC中对应名称的bean和bean的类型
        for (String name : definitionNames) {
            // 这个会影响到测试懒加载的效果，如果需要测试懒加载，这行代码需要注释掉，因为getBean方法一旦调用则会初始化
            Object bean = applicationContext.getBean(name);
            System.out.println("bean name:" + name + ", type: " + bean.getClass());
        }
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    public static void testPrototypeScopeService(ApplicationContext applicationContext) {
        System.out.println("\n-----------------------测试@Scope开始-------------------------");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserService userService1 = applicationContext.getBean(UserService.class);
        System.out.println("默认单实例bean UserService是否相等 " + (userService == userService1));

        PrototypeScopeService prototypeScopeService = applicationContext.getBean(PrototypeScopeService.class);
        PrototypeScopeService prototypeScopeService1 = applicationContext.getBean(PrototypeScopeService.class);
        System.out.println("PrototypeScopeService prototype scope作用域是否相等: " + (prototypeScopeService == prototypeScopeService1));
        System.out.println("-----------------------测试@Scope结束-------------------------\n");
    }

    /**
     * 测试单实例bean的懒加载，只有等使用的时候再创建实例。
     * IOC容器启动后不会创建该bean的实例，如果是在该方法中才创建这个bean的实例，并且获得的两个bean是同一个的话，则测试通过。
     */
    public static void testLazyBeanService(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试单实例bean的@Lazy懒加载开始----------------------");
        LazyBeanService lazyBeanService = applicationContext.getBean(LazyBeanService.class);
        LazyBeanService lazyBeanService1 = applicationContext.getBean(LazyBeanService.class);
        System.out.println("lazyBeanService==lazyBeanService1？: " + (lazyBeanService == lazyBeanService1));
        System.out.println("---------------测试单实例bean的@Lazy懒加载结束----------------------\n");
    }


    /**
     * 测试通过FactoryBean接口导入单实例与Prototype作用域的组件,根据打印可以看出FactoryBean创建的单实例Bean都是懒加载的
     * @param applicationContext
     */
    public static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
        System.out.println("\n----------测试通过FactoryBean注册单实例和Prototype作用域的组件开始----------");
        FactorySingletonBean factorySingletonBean = applicationContext.getBean(FactorySingletonBean.class);
        FactorySingletonBean factorySingletonBean1 = applicationContext.getBean(FactorySingletonBean.class);

        FactoryPrototypeBean factoryPrototypeBean = applicationContext.getBean(FactoryPrototypeBean.class);
        FactoryPrototypeBean factoryPrototypeBean1 = applicationContext.getBean(FactoryPrototypeBean.class);

        System.out.println("单实例factorySingletonBean==factorySingletonBean1?" + (factorySingletonBean==factorySingletonBean1));

        System.out.println("Prototype作用域factoryPrototypeBean==factoryPrototypeBean1?" + (factoryPrototypeBean==factoryPrototypeBean1));
        System.out.println("----------测试通过FactoryBean注册单实例和Prototype作用域的组件结束----------\n");
    }

    /**
     * 测试通过@PropertySource和@Value注解来对属性进行赋值
     * @param applicationContext
     */
    public static void testPropertySourceValue(ApplicationContext applicationContext) {
        System.out.println("\n---------------测试@PropertySource和@Value赋值开始----------------");
        User user = applicationContext.getBean(User.class);
        System.out.println("user属性为： " + user.toString());
        System.out.println("---------------测试@PropertySource和@Value赋值结束----------------\n");

    }

    /**
     * 测试在IOC容器中存在两个相同类型的Bean,但是Bean的名称不一致
     * 在这种情况下，使用@Autowired将该Bean注入到另外一个容器中
     * @Autowired 默认使用Bean的类型去匹配注入，如果找到多个相同类型的Bean,则使用默认名称去获取Bean,Bean的默认名称为类首字母缩写
     * 也可以配合@Autowired配合@Qualifier注解明确指定注入哪个Bean，还可以在注入Bean的地方使用@Primary，在不指定@Qualifier的情况下，
     * 默认注入哪个Bean {@link AutowiredService}
     * @param applicationContext
     */
    public static void testAutowired(ApplicationContext applicationContext) {
        System.out.println("\n--------------测试autowired注入多个相同类型的类开始-----------------");
        AutowiredBean autowiredBean = (AutowiredBean) applicationContext.getBean("autowiredBean");
        AutowiredBean autowiredBean2 = (AutowiredBean) applicationContext.getBean("autowiredBean2");
        System.out.println("autowiredBean: " + autowiredBean);
        System.out.println("autowiredBean2: " + autowiredBean2);
        System.out.println(autowiredBean == autowiredBean2);

        /**
         * 这里已做更改，修改了默认注入 {@link com.ddf.spring.annotation.configuration.AnnotationConfiguration.autowiredBean2}
         */
        AutowiredService autowiredService = applicationContext.getBean(AutowiredService.class);
        AutowiredBean autowiredServiceBean = autowiredService.getAutowiredBean();
        System.out.println("使用@Primay后AutowiredService默认注入bean: " + autowiredServiceBean);

        AutowiredBean autowiredServiceBean2 = autowiredService.getQualifierAutowiredBean();
        System.out.println("使用@Qualifier明确注入Bean: " + autowiredServiceBean2);

        // 使用@Resource注入
        AutowiredBean resourceAutowiredBean = autowiredService.getResourceAutowiredBean();
        System.out.println("使用@Resource注入autowiredBean: " + resourceAutowiredBean);
        AutowiredBean resourceAutowiredBean2 = autowiredService.getResourceAutowiredBean2();
        System.out.println("使用@Resource注入autowiredBean2: " + resourceAutowiredBean2);

        // 使用@Inject注入
        UserService userService = autowiredService.getUserService();
        System.out.println("使用@Inject注入UserService： " + userService);

        System.out.println("--------------测试autowired注入多个相同类型的类结束-----------------\n");
    }


    /**
     * 测试根据激活的Profile来根据环境注册不同的Bean
     * 切换profile有两种方式：
     * 1. 在java虚拟机启动参数加 -Dspring.profiles.active=test
     * 2. 如下演示，使用代码切换，可以将切换的变量放在配置文件，spring-boot配置文件即是这种方式
     * @param applicationContext
     */
    public static void testProfile(AnnotationConfigApplicationContext applicationContext) {
        System.out.println("\n------------------测试@Profile开始-------------------------");
        // 重新新建一个IOC容器
        applicationContext = new AnnotationConfigApplicationContext();
        // 注册配置类
        applicationContext.register(ProfileConfiguration.class);
        // 设置当前激活的环境profile
        applicationContext.getEnvironment().setActiveProfiles("dev");
        // 刷新容器
        applicationContext.refresh();

        // 使用接口获得实际接口实现类的注入Bean
        Slf4jBean devLogBean = applicationContext.getBean(Slf4jBean.class);
        devLogBean.info("测试环境");

        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileConfiguration.class);
        applicationContext.getEnvironment().setActiveProfiles("prd");
        applicationContext.refresh();

        Slf4jBean prdLogBean = applicationContext.getBean(Slf4jBean.class);
        prdLogBean.info("生产环境");

        System.out.println("------------------测试@Profile结束-------------------------\n");
    }


    /**
     * 测试AOP
     * @param applicationContext
     */
    public static void testAspect(ApplicationContext applicationContext) {
        System.out.println("\n--------------------测试AOP开始----------------------------");

        UserService userService = applicationContext.getBean(UserService.class);
        userService.welcome("ddf");
        try {
            userService.welcomeException("ddf");
        } catch (Exception e) {
        }

        System.out.println("--------------------测试AOP结束----------------------------\n");
    }


    /**
     * 测试连接数据源
     */
    public static void testDruidDataSource(ApplicationContext applicationContext) {
        System.out.println("\n--------------------获取DruidDataSource数据库连接开始----------------------------");
        DataSourceConnection dataSourceConnection = applicationContext.getBean(DataSourceConnection.class);
        System.out.println(dataSourceConnection);
        DruidDataSource druidDataSource = applicationContext.getBean(DruidDataSource.class);
        DruidPooledConnection connection = null;
        try {
            connection = druidDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(connection);
        System.out.println("--------------------获取DruidDataSource数据库连接结束----------------------------\n");
    }


    /**
     * 测试增加一个Person
     * 使用事务和不使用事务对比
     * @param applicationContext
     */
    public static void testAddPerson(ApplicationContext applicationContext) {
        System.out.println("\n-------------------测试增加一个Person开始-------------------------");
        PersonService personService = applicationContext.getBean(PersonService.class);
        Calendar calendar = new GregorianCalendar();
        calendar.set(1992, 4, 29);
        try {
            personService.add(new Person("no_transactional", calendar.getTime(), "上海市", "18356789999"));
        } catch (Exception e) {}
        try {
            personService.addWithTransactional(new Person("with_transactional", calendar.getTime(), "上海市", "18356789999"));
        } catch (Exception e) {}
        System.out.println("-------------------测试增加一个Person结束-------------------------\n");
    }
}
```
IIII. 运行主启动类，可以看到日志如下，省略不相关其它日志,数据库连接正常连接，然后测试保存的方法里每一个都抛出了异常，但是查看数据库记录，只有第一个用户`no_transactional`保存了下来，另外一条记录回滚并没有保存下来
```txt
--------------------获取DruidDataSource数据库连接开始----------------------------
DataSourceConnection{name='druid', userName='root', password='123456', driverClassName='com.mysql.jdbc.Driver', url='jdbc:mysql://localhost:3306/spring-annotation?characterEncoding=utf8&useSSL=true'}
八月 14, 2018 1:09:14 下午 com.alibaba.druid.pool.DruidDataSource info
信息: {dataSource-1,druid} inited
com.mysql.jdbc.JDBC4Connection@36cda2c2
--------------------获取DruidDataSource数据库连接结束----------------------------


-------------------测试增加一个Person开始-------------------------
add开始执行,参数列表[Person{id=null, name='no_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]
add结束执行,参数列表[Person{id=null, name='no_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]
add结束执行,参数列表[Person{id=null, name='no_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]，出现异常: java.lang.RuntimeException: 抛出异常
addWithTransactional开始执行,参数列表[Person{id=null, name='with_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]
addWithTransactional结束执行,参数列表[Person{id=null, name='with_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]
addWithTransactional结束执行,参数列表[Person{id=null, name='with_transactional', birthDay=Fri May 29 13:09:14 CST 1992, address='上海市', tel='18356789999'}]，出现异常: java.lang.RuntimeException: 抛出异常
-------------------测试增加一个Person结束-------------------------
```




## 三、扩展原理
### 1. `BeanFactoryPostProcessor`接口
> BeanFactoryPostProcessor
>>  PropertyResourceConfigurer   // 配置文件方式覆盖bean属性
>> BeanDefinitionRegistryPostProcessor // 注册更多的bean

> BeanPostProcessor 直接对属性改变，建议使用这个接口

```
/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Allows for custom modification of an application context's bean definitions,
 * adapting the bean property values of the context's underlying bean factory.
 *
 * <p>Application contexts can auto-detect BeanFactoryPostProcessor beans in
 * their bean definitions and apply them before any other beans get created.
 *
 * <p>Useful for custom config files targeted at system administrators that
 * override bean properties configured in the application context.
 *
 * <p>See PropertyResourceConfigurer and its concrete implementations
 * for out-of-the-box solutions that address such configuration needs.
 *
 * <p>A BeanFactoryPostProcessor may interact with and modify bean
 * definitions, but never bean instances. Doing so may cause premature bean
 * instantiation, violating the container and causing unintended side-effects.
 * If bean instance interaction is required, consider implementing
 * {@link BeanPostProcessor} instead.
 *
 * @author Juergen Hoeller
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */
public interface BeanFactoryPostProcessor {

 /**
  * Modify the application context's internal bean factory after its standard
  * initialization. All bean definitions will have been loaded, but no beans
  * will have been instantiated yet. This allows for overriding or adding
  * properties even to eager-initializing beans.
  * @param beanFactory the bean factory used by the application context
  * @throws org.springframework.beans.BeansException in case of errors
  */
 void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
```

I. 可以获取到所有的`Bean`预定义，但绝不是初始化，说明这个时候的`Bean`只是一些定义信息，可以获取到`beanFactory`，而beanFactory就是根据预定义的`Bean`信息去进行`Bean`的初始化，所以这里允许修改`Bean`的预定义的信息，以达到在`bean`被初始化之前做一些属性的改变等.如果是希望对`bean`的属性进行改变,推荐使用接口`BeanPostProcessor`代替

II.`org.springframework.beans.factory.config.PropertyResourceConfigurer`是`BeanFactoryPostProcessor`的子类接口，允许从属性文件的方式来配置`bean`的值，属性值可以在读取后通过覆盖进行转换,见方法`convertPropertyValue`, 该接口提供了两个实现
* PropertyOverrideConfigurer ,for "beanName.property=value" style overriding
* PropertyPlaceholderConfigurer, for replacing "${...}" placeholders

III. `org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor`，这个接口扩展自接口`BeanFactoryPostProcessor`,这个接口不仅可以获取已经预定义的`bean`信息，更多的用于可以在`bean`被实例化之前注册更多的`bean`定义，然后反向注册到`beanFactory`中，这样就可以实例化更多的`Bean`


## 四、Servlet3.0
本章节不详细介绍相关servlet的只是，只大概讲解如果抛弃传统的`web.xml`配置文件来注册servlet三大组件
> Servlet3.0以后,引入一个接口，`javax.servlet.ServletContainerInitializer`，该接口的`onStartup`方法可以在`ServletContext`启动完成之前，创建组件，并且实现了该接口的类通过特定格式的配置，能够在Servlet容器启动的时候自动被识别到然后被加载，最终完成组件的创建工作，包含每一个jar包里的ServletContainerInitializer的实现。格式规则必须绑定在classpath下，`META-INF/services/javax.servlet.ServletContainerInitializer`文件下META-INF/services/这一块为文件路径，`javax.servlet.ServletContainerInitializer`这个是文件的名称，而不是类名，然后将实现了`ServletContainerInitializer`接口的全类名粘贴到这个文件内容里即可。使用`IDEA`直接新建`package`META-INF会不能直接创建，但是可以直接创建`META-INF/services`，然后再在这个路径下新建文件即可

### 1. 创建一个`Servlet`工程，勾选`Web Application`，取名`servlet-annotation`
### 2. 创建基于注解的`Servlet`,对于`Servlet`提供直接的注解支持`WebServlet`
```java
package com.ddf.annotation.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DDf on 2018/8/14
 * 使用注解代替web.xml配置文件来识别Servlet
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("hello!");
    }
}
```
### 3. 编写待注册的`Servlet`组件，不使用`WebServlet`注解
```java
package com.ddf.annotation.servlet;

import com.ddf.annotation.servlet.configuration.CustomsContainerInit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DDf on 2018/8/14
 * 使用编码方式映射servlet {@link CustomsContainerInit}
 */
public class CustomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("使用编码方式映射Servlet..........");
    }
}
```

### 4. 编写`Filter`
```java
package com.ddf.annotation.servlet.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author DDf on 2018/8/14
 */
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化。。。。。。。。");
        System.out.println("servlet容器： " + filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("目标方法被拦截.............");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("销毁方法。。。。。。。。。。。");
    }
}
```
### 5. 编写`Listener`
```java
package com.ddf.annotation.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author DDf on 2018/8/14
 * 监听servlet容器的创建和销毁
 */
public class CustomListeners implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("监听容器初始化。。。。。。。。。");
        System.out.println(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("监听容器销毁..........");
        System.out.println(servletContextEvent);
    }
}
```

### 6. 编写注册三大组件的配置类，实现`ServletContainerInitializer`接口
```java
package com.ddf.annotation.servlet.configuration;

import com.ddf.annotation.servlet.CustomServlet;
import com.ddf.annotation.servlet.filter.CustomFilter;
import com.ddf.annotation.servlet.listener.CustomListeners;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author DDf on 2018/8/14
 * @HandlesTypes 容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来作为onStartup的第一个参数
 *
 * 1、Servlet容器启动会扫描，当前应用里面每一个jar包的
 * ServletContainerInitializer的实现
 * 2、提供ServletContainerInitializer的实现类；
 * 必须绑定在classpath下，META-INF/services/javax.servlet.ServletContainerInitializer
 * 文件的内容就是ServletContainerInitializer实现类的全类名；
 */
@HandlesTypes(value={Filter.class})
public class CustomsContainerInit implements ServletContainerInitializer {


    /**
     * 在容器启动的时候创建servlet组件，不需要使用配置文件
     * @param set
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("@HandlesTypes传入的类型有: ");
        for (Class clazz : set) {
            System.out.println(clazz);
        }

        // 注册Servlet
        ServletRegistration.Dynamic customServlet = servletContext.addServlet("customServlet", new CustomServlet());
        customServlet.addMapping("/custom");

        // 注册Listener
        servletContext.addListener(CustomListeners.class);

        // 注册Filter
        FilterRegistration.Dynamic customFilter = servletContext.addFilter("customFilter", new CustomFilter());
        customFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

    }
}
```

### 7. 识别`ServletContainerInitializer`实现类
在`src`下新建`META-INF\services\javax.servlet.ServletContainerInitializer`文件,粘贴自定义实现类的全类名
```
com.ddf.annotation.servlet.configuration.CustomsContainerInit
```

### 8. 启动容器,访问`ip:port/servlet-annotation/custom`,然后销毁容器，日志如下，正确识别
```
@HandlesTypes传入的类型有: 
class com.ddf.annotation.servlet.filter.CustomFilter
监听容器初始化。。。。。。。。。
javax.servlet.ServletContextEvent[source=org.apache.catalina.core.StandardContext$NoPluggabilityServletContext@1e99db28]
过滤器初始化。。。。。。。。
servlet容器： org.apache.catalina.core.ApplicationContextFacade@1af646c8
目标方法被拦截.............
目标方法被拦截.............
14-Aug-2018 16:29:26.003 信息 [localhost-startStop-1] org.apache.catalina.startup.HostConfig.deployDirectory Deploying web application directory D:\tomcat8\webapps\manager
14-Aug-2018 16:29:26.039 信息 [localhost-startStop-1] org.apache.catalina.startup.HostConfig.deployDirectory Deployment of web application directory D:\tomcat8\webapps\manager has finished in 36 ms
目标方法被拦截.............
销毁方法。。。。。。。。。。。
监听容器销毁..........
javax.servlet.ServletContextEvent[source=org.apache.catalina.core.StandardContext$NoPluggabilityServletContext@1e99db28]
```

