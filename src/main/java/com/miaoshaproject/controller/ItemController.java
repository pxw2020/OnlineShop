package com.miaoshaproject.controller;

import com.miaoshaproject.controller.basecontroller.BaseController;
import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.model.ItemModel;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemServiceImpl;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = "*",allowCredentials = "true")
public class ItemController extends BaseController {


    @Autowired
    private ItemServiceImpl itemService;

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "price") Double price,
                                       @RequestParam(name = "imgUrl") String imgUrl,
                                       @RequestParam(name = "description") String description) throws BusinessException {

        //封装service请求
        ItemModel itemModel = new ItemModel();
        itemModel.setStock(stock);
        itemModel.setPrice(new BigDecimal(price));
        itemModel.setDescription(description);
        itemModel.setTitle(title);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelResult = itemService.createItem(itemModel);
        ItemVO itemVO = convertFromItemModel(itemModelResult);
        return CommonReturnType.create(itemVO);
    }



    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    //   @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id){

        ItemModel itemModelResult = itemService.getItemByID(id);
        ItemVO itemVO = convertFromItemModel(itemModelResult);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertFromItemModel(ItemModel itemModel) {
        if (itemModel==null)
            return null;
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if (itemModel.getPromoModel()!=null){
            //有正在进行或即将开始的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setEndDate(itemModel.getPromoModel().getEndDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else {
            itemVO.setPromoStatus(0);
        }

         return itemVO;
    }


    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {


        List<ItemModel> itemModels = itemService.listItem();
        List<ItemVO> itemVOs = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = convertFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOs);
    }


}
