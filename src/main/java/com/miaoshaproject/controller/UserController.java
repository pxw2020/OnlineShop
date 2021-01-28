package com.miaoshaproject.controller;

import com.miaoshaproject.controller.basecontroller.BaseController;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.UserModel;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.UserServiceImpl;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 14:50
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    //用户注册接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam("password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        //TODO 入参校验

        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }


        if (org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }

        String EncrptPassword = EncodeByMD5(password);

        UserModel userModel = userService.validateLogin(telephone,EncrptPassword);

        //将登陆成功凭证加入session
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return new CommonReturnType().create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST})
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam("otpCode") String otpCode,
                                     @RequestParam("name") String name,
                                     @RequestParam("gender") Integer gender,
                                     @RequestParam("age") Integer age,
                                     @RequestParam("password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        // 验证手机号和otpCode是否匹配

        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)) {
            throw  new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
        }
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setTelephone(telephone);
        //TODO 关联第三方ID、还没做
        userModel.setThirdPartyId("1");
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));
        userService.register(userModel);
        return new CommonReturnType().create(null);
    }

    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
    }
    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone) {
        //按照一定规则生成otp验证码
        System.out.println("进来了");
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String otpCode = String.valueOf(randomInt);


        //TODO 将验证码和对应用户手机号关联(redis存储-适合分布式）
        //暂时使用httpSession绑定opt和手机号
        httpServletRequest.getSession().setAttribute(telephone,otpCode);
        String s = (String) httpServletRequest.getSession().getAttribute(telephone);
        //TODO 通过短信渠道发送otp验证码（省略）

        System.out.println(""+telephone+"  otp短信验证码："+otpCode);
        return CommonReturnType.create(null);
    }



    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {

        UserModel userModel = userService.getUserByID(id);
        if(userModel==null) {
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
