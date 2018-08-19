package com.ddf.springmvc.annotation.controller;

import com.ddf.springmvc.annotation.configuration.exception.GlobalException;
import com.ddf.springmvc.annotation.configuration.exception.GlobalExceptionEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author DDf on 2018/8/18
 */
@Controller
@RequestMapping("exception")
public class ExceptionController {

    /**
     * 抛出非GlobalException的异常也可以处理
     */
    @RequestMapping("throwString")
    public void throwString() {
        throw new RuntimeException("运行出错");
    }

    /**
     * 抛出GlobalException，code需要在实现了GlobalExceptionCodeResolver接口的类中定义，该code还需要在资源文件exception.properties中定义
     * 对应的value来作为消息解析  /resources/exception/exception.properties  exception_en_US.properties exception_ch_CN.properties
     */
    @RequestMapping("throwCode")
    public void throwCode() {
        throw new GlobalException(GlobalExceptionEnum.SYS_ERROR);
    }


    /**
     * 抛出GlobalException，code需要在实现了GlobalExceptionCodeResolver接口的类中定义，该code还需要在资源文件exception.properties中定义
     * 对应的value来作为消息解析  /resources/exception/exception.properties  exception_en_US.properties exception_ch_CN.properties
     * ,资源文件中对应key的value可以使用{0}{1}占位符，然后在构造函数中传入替换的值，即可替换
     */
    @RequestMapping("throwCodeParam")
    public void throwCodeParam() {
        throw new GlobalException(GlobalExceptionEnum.PLACEHOLDER_DEMO, System.currentTimeMillis(),
                System.currentTimeMillis());
    }
}
