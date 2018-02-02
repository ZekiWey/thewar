package com.lw.user.service;

import com.lw.common.ServerResponse;

/**
 * Created by Administrator on 2018/1/2.
 */
public interface IUserService {

    public ServerResponse login(String telphone, String password);

    public ServerResponse registered_checkTelphone(String telphone);

    public ServerResponse registered_checkCode(String code, String telphone, String password, String hxUserName);


    public ServerResponse checkTelphoneReg(String telphone);
}
