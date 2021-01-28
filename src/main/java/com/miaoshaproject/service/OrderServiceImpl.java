package com.miaoshaproject.service;

import com.miaoshaproject.dao.*;
import com.miaoshaproject.dataobject.*;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.ItemModel;
import com.miaoshaproject.model.OrderModel;
import com.miaoshaproject.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {

        //1。校验下单状态  商品是否存在 用户是否合法 购买数量是否正确
        ItemModel itemModel = itemService.getItemByID(itemId);
        if (itemModel==null){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }

        UserModel  userModel = userService.getUserByID(userId);
        if (userModel==null){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"该用户不存在");
        }
        if (amount<=0||amount>99) {
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"购买数量不正确");
        }
        //2.落单减库存 支付减库存
        Boolean aBoolean = itemService.decreaseStock(itemId,amount);
        if (!aBoolean)
            throw new BusinessException(EmBussinessError.STOCK_NOT_ENOUGH);

        //生成订单号

        String ID = generateOrderNo();
        
        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setId(ID);
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(BigDecimal.valueOf(amount)));
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //增加销量
        itemService.increaseSales(itemId,amount);
        return orderModel;
    }

    //事务内部重开事务 单独提交
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo() {
        //订单号16位
        //前八位 年月日
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate= now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间六位自增序列
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0;i<6-sequenceStr.length();i++)
            stringBuilder.append("0");
        stringBuilder.append(sequenceStr);
            //最后两位 分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel==null)
            return null;
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        return orderDO;
    }
}
