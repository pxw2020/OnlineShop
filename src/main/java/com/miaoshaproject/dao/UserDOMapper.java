package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserDO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    UserDO selectByPrimaryKey(Integer id);
    UserDO selectByTelephone(String telephone);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Tue Jan 19 12:16:48 GMT+08:00 2021
     */
    int updateByPrimaryKey(UserDO record);
}