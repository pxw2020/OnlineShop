package com.miaoshaproject.service;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 14:58
 */
@Service
public class UserServiceImpl implements UserService {

   @Autowired
   private UserDOMapper userDOMapper;

   @Autowired
   private UserPasswordDOMapper userPasswordDOMapper;
    @Override
    public UserModel getUserByID(Integer id) {

        UserDO user = userDOMapper.selectByPrimaryKey(id);
        if(user==null)
            return null;
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(user.getId());

        return convertFromDataObject(user,userPasswordDO);

    }

    UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {

        if(userDO==null)
            return null;

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null)
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
