package com.ddf.springmvc.annotation.configuration.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DDf on 2018/8/17
 */
@Component
@RequestScope
public class RequestContext {

    private Map<String, Object> paramsMap = new HashMap<>();

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }
}
