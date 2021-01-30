package com.miaoshaproject.controller;

import com.miaoshaproject.controller.basecontroller.BaseController;
import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.ItemModel;
import com.miaoshaproject.model.UserModel;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {


    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private OrderServiceImpl orderService;

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId",required = false) Integer promoId) throws BusinessException {


        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin==null||!isLogin.booleanValue()){
            throw new BusinessException(EmBussinessError.USER_NOT_LOGIN,"用户还未登录");
        }
        //获取用户登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        if (userModel==null){
            throw new BusinessException(EmBussinessError.USER_NOT_LOGIN);
        }
        //1.入参校验 商品是否存在 数量是否超过库存

        //封装service请求
       orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        return CommonReturnType.create(null);
    }
}
