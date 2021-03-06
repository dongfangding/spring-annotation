package com.ddf.springmvc.annotation.configuration.exception;

/**
 * @author DDf on 2018/8/18
 * 为自定义异常类消息代码定义统一接口，所以定义消息代码的类必须实现这个接口
 */
public interface GlobalExceptionCodeResolver {
    String getCode();
}
