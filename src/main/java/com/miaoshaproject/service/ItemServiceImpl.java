package com.miaoshaproject.service;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.ItemModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private ValidatorImpl validation;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        //校验入参
       ValidationResult validationResult =  validation.validate(itemModel);
       if (validationResult.isHasErrors()){
           throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
       }
       //转化itemModel->objectModel
        ItemDO itemDO = convertFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);

        //顺序很重要  必须第一次插入成功才能拿到商品id，给库存表用
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = convertFromItemStockModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完对象
        return this.getItemByID(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {

        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModels = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemID(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModels;
    }


    @Override
    public ItemModel getItemByID(Integer id) {

        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemID(itemDO.getId());
        ItemModel itemModel = convertModelFromDataObject(itemDO,itemStockDO);
        return itemModel;
    }


    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    private ItemDO convertFromItemModel(ItemModel itemModel) {
        if (itemModel==null)
            return null;
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }
    private ItemStockDO convertFromItemStockModel(ItemModel itemModel) {

        if (itemModel==null)
            return null;
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setStock(itemModel.getStock());
        itemStockDO.setItemId(itemModel.getId());
        return itemStockDO;
    }

}
