package com.ddf.springmvc.annotation.configuration.exception;

import org.springframework.core.NestedRuntimeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DDf on 2018/8/18
 * 自定义异常类
 */
public class GlobalException extends NestedRuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    // 可替换消息参数，消息配置中不确定的值用大括号包着数组角标的方式，如{0} 占位，抛出异常的时候使用带params的构造函数赋值，即可替换
    private Object[] params;

    public GlobalException(String msg) {
        super(msg);
        this.code = msg;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        return map;
    }

    /**
     * @param enumClass
     * @param params
     */
    public GlobalException(GlobalExceptionCodeResolver codeResolver, Object... params) {
        super(codeResolver.getCode());
        this.code = codeResolver.getCode();
        this.params = params;
    }

    public GlobalException(GlobalExceptionCodeResolver codeResolver) {
        super(codeResolver.getCode());
        this.code = codeResolver.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
