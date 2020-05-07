package com.ddf.spring.annotation.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

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
@Component
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
