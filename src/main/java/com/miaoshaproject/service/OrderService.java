package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.model.OrderModel;

public interface OrderService {

    //1.通过前端传来秒杀活动id 然后下单接口校验对应id是否属于对应商品

    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
