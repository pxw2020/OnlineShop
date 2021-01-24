package com.miaoshaproject.service;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 14:58
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ValidatorImpl validator;
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

    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel==null) {
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult validationResult = validator.validate(userModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }

        UserDO userDO = convertFromUserModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException de) {
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"该手机号已经注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromUserModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telephone, String EncreptPassword) throws BusinessException {
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if (userDO==null) {
            throw new BusinessException(EmBussinessError.USER_LOGIN_FAIL,"用户不存在");
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        if (!StringUtils.equals(userPasswordDO.getEncrptPassword(),EncreptPassword)) {
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        return userModel;
    }


    private UserPasswordDO convertPasswordFromUserModel(UserModel userModel) {

        if(userModel==null)
            return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromUserModel(UserModel userModel) {

        if(userModel==null)
            return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {

        if(userDO==null)
            return null;

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null)
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
