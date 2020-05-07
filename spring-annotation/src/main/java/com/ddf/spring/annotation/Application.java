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
import java.util.Calendar;
import java.util.GregorianCalendar;

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
        testProfile();
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
            System.out.println("bean name:" + name + ", type: " + bean.getClass() + "bean: " + bean);
        }
    }


    /**
     * 测试@Scope bean的作用域
     *
     * @param applicationContext
     */
    private static void testPrototypeScopeService(ApplicationContext applicationContext) {
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
    private static void testLazyBeanService(ApplicationContext applicationContext) {
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
    private static void testFactoryBeanPrototypeBean(ApplicationContext applicationContext) {
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
    private static void testPropertySourceValue(ApplicationContext applicationContext) {
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
    private static void testAutowired(ApplicationContext applicationContext) {
        System.out.println("\n--------------测试autowired注入多个相同类型的类开始-----------------");
        AutowiredBean autowiredBean = (AutowiredBean) applicationContext.getBean("autowiredBean");
        AutowiredBean autowiredBean2 = (AutowiredBean) applicationContext.getBean("autowiredBean2");
        System.out.println("autowiredBean: " + autowiredBean);
        System.out.println("autowiredBean2: " + autowiredBean2);
        System.out.println(autowiredBean == autowiredBean2);

        /**
         * 这里已做更改，修改了默认注入 {@link AnnotationConfiguration.autowiredBean2}
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
     */
    private static void testProfile() {
        System.out.println("\n------------------测试@Profile开始-------------------------");
        // 重新新建一个IOC容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
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
    private static void testAspect(ApplicationContext applicationContext) {
        System.out.println("\n--------------------测试AOP开始----------------------------");

        UserService userService = applicationContext.getBean(UserService.class);
        userService.welcome("ddf");
        try {
            userService.welcomeException("ddf");
        } catch (Exception ignored) {
        }

        System.out.println("--------------------测试AOP结束----------------------------\n");
    }


    /**
     * 测试连接数据源
     */
    private static void testDruidDataSource(ApplicationContext applicationContext) {
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
    private static void testAddPerson(ApplicationContext applicationContext) {
        System.out.println("\n-------------------测试增加一个Person开始-------------------------");
        PersonService personService = applicationContext.getBean(PersonService.class);
        Calendar calendar = new GregorianCalendar();
        calendar.set(1992, Calendar.MAY, 29);
        try {
            personService.add(new Person("no_transactional", calendar.getTime(), "上海市", "18356789999"));
        } catch (Exception ignored) {}
        try {
            personService.addWithTransactional(new Person("with_transactional", calendar.getTime(), "上海市", "18356789999"));
        } catch (Exception ignored) {}
        System.out.println("-------------------测试增加一个Person结束-------------------------\n");
    }

}