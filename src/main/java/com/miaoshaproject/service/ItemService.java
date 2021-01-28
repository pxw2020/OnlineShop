package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    List<ItemModel> listItem();
    ItemModel getItemByID(Integer id);
    boolean decreaseStock(Integer itemId,Integer amount);
    void increaseSales(Integer itemId,Integer amount);
}
