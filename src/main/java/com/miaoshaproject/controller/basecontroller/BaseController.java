package com.miaoshaproject.controller.basecontroller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController{


    public final static String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";
    //定义一个ExceptionHandler 解决未被controller层吸收的异常

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest httpServletRequest, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errMsg", businessException.getErrMsg());
            responseData.put("errCode", businessException.getErrCode());
        } else {
            responseData.put("errMsg", EmBussinessError.UNKNOWN_ERROR.getErrMsg());
            responseData.put("errCode", EmBussinessError.UNKNOWN_ERROR.getErrCode());
        }
        return CommonReturnType.create(responseData, "fail");

    }
}
