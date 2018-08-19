package com.ddf.springmvc.annotation.configuration.exception;

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
import com.ddf.springmvc.annotation.configuration.ioc.RootApplicationContextConfig;

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
