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

    @RequestMapping("throwString")
    public void throwString() {
        throw new RuntimeException("运行出错");
    }

    @RequestMapping("throwCode")
    public void throwCode() {
        throw new GlobalException(GlobalExceptionEnum.SYS_ERROR);
    }

    @RequestMapping("throwCodeParam")
    public void throwCodeParam() {
        throw new GlobalException(GlobalExceptionEnum.PLACEHOLDER_DEMO, System.currentTimeMillis(),
                System.currentTimeMillis());
    }

    @RequestMapping("throwCodeParamStatus")
    public void throwCodeParamStatus() {
        throw new GlobalException(GlobalExceptionEnum.PLACEHOLDER_DEMO, System.currentTimeMillis(),
                System.currentTimeMillis(), HttpStatus.OK);
    }
}
