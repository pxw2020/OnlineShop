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
import java.util.Random;

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

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户获取otp短信接口
    @RequestMapping("/getotp")
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone) {
        //按照一定规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String otpCode = String.valueOf(randomInt);

        //TODO 将验证码和对应用户手机号关联(redis存储-适合分布式）
        //暂时使用httpSession绑定opt和手机号
        httpServletRequest.getSession().setAttribute(telephone,otpCode);

        //TODO 通过短信渠道发送otp验证码（省略）

        System.out.println(""+telephone+"  otp短信验证码："+otpCode);
        return null;
    }



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
