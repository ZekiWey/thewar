package com.lw.user.dao;

import com.lw.pojo.UserLoginPhone;
import org.apache.ibatis.annotations.Param;

public interface UserLoginPhoneMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoginPhone record);

    int insertSelective(UserLoginPhone record);

    UserLoginPhone selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLoginPhone record);

    int updateByPrimaryKey(UserLoginPhone record);

    UserLoginPhone selectcheckUserLogin(@Param("telphone") String telphone, @Param("password") String password);

    int selectCheckTelphone(String telphone);
}