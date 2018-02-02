package com.lw.user.dao;

import com.lw.pojo.UserPlugins;

public interface UserPluginsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPlugins record);

    int insertSelective(UserPlugins record);

    UserPlugins selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPlugins record);

    int updateByPrimaryKey(UserPlugins record);

    int selectHXUsername(String hxUserName);
}