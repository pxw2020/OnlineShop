package com.miaoshaproject.controller;

import com.miaoshaproject.controller.basecontroller.BaseController;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.UserModel;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 14:50
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {

        UserModel userModel = userService.getUserByID(id);
        if(userModel==null) {
            //userModel.setEncrptPassword("22");
            throw new BusinessException(EmBussinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    public UserVO convertFromModel(UserModel userModel){

        if (userModel==null)
           return null;

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
