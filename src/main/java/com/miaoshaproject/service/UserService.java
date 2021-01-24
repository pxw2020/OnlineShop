package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.model.UserModel;

/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 14:54
 */
public interface UserService {
    UserModel getUserByID(Integer id);
    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String telephone, String password) throws BusinessException;
}
