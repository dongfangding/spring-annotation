package com.ddf.springmvc.annotation.configuration.exception;

import com.ddf.springmvc.annotation.configuration.ioc.RootApplicationContextConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author DDf on 2018/8/18
 * 处理全局异常，将异常消息使用json返回到前端
 * 该类需要交由容器管理，为方便统一辨识，没有在本类使用注解，参见{@link RootApplicationContextConfig#globalExceptionHandler()}
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageSource messageSource;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType("application/json;charset=UTF-8");
        Locale locale = request.getLocale();
        GlobalException globalException;
        if (ex instanceof GlobalException) {
            globalException = (GlobalException) ex;
        } else {
            globalException = new GlobalException(ex.getMessage());
            globalException.setCode(ex.getMessage());
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // 使用messageSource将code到资源文件中根据locale解析成对应的消息文件，并填充占位符。getMessage()重载了多个方法，
        // 如果不存在可以抛异常，也可以 给一个默认值，这里使用了给默认值的处理，默认值即是code
        globalException.setMessage(messageSource.getMessage(globalException.getCode(), globalException.getParams(),
                globalException.getCode(), locale));
        try {
            response.getWriter().write(objectMapper.writeValueAsString(globalException.toMap()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
