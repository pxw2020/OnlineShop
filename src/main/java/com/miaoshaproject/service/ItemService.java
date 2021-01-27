package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    public ItemModel createItem(ItemModel itemModel) throws BusinessException;
    public List<ItemModel> listItem();
    public ItemModel getItemByID(Integer id);
}
