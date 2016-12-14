package com.yay.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: 异常处理
 * @author Yay
 * @version 1.0
 * @since 2016/12/1 15:43
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private Logger log = LogManager.getLogger("ExceptionControllerAdvice");

    @ExceptionHandler
    public String expAdvice(HttpServletRequest req, Exception ex){
        req.setAttribute("ex", ex.getMessage());
        log.error(ex);
        //返回错误提示页面，实际开发时可根据不同的异常类型区别对待。
        return "common/error";
    }
}