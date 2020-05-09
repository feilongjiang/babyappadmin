package com.example.happybaby.controller;


import com.example.happybaby.exception.BaseHttpStatus;
import com.example.happybaby.exception.ErrorCode;
import com.example.happybaby.exception.MyException;
import com.example.happybaby.utils.APIResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


@ControllerAdvice
public class ControllerAdviceProcessor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody

    /**
     * @method  handleException
     * @description 描述一下方法的作用
     * @date: 2020-03-27 11:26
     * @param request
     * @param e
     * @return com.example.happybaby.utils.APIResult
     */
    public APIResult handleException(HttpServletRequest request, Exception e) {
        ErrorCode status = BaseHttpStatus.NOT_IMPLEMENTED;
        if (e instanceof HttpMessageNotReadableException) {
            status = BaseHttpStatus.BAD_REQUEST;
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            status = BaseHttpStatus.NOT_IMPLEMENTED;
        }
        String msg = null;


        if (e instanceof MyException) {
            MyException MyException = (MyException) e;
            msg = MyException.getMessage();
            status = MyException.getErrorCode();

        } else if (e instanceof AccessDeniedException) {
            msg = "无权限访问";
            status = BaseHttpStatus.FORBIDDEN;
        }
        if (msg == null) {
            msg = e.getMessage();
        }

        return APIResult.create((BaseHttpStatus) status, msg);
    }
}
