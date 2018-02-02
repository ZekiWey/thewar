package com.lw.user.controller;

import com.lw.common.Const;
import com.lw.common.ResponseCode;
import com.lw.common.ServerResponse;
import com.lw.common.SessionListener;
import com.lw.user.service.IUserService;
import com.lw.vo.UserLoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/1/2.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Resource(name = "iUserService")
    private IUserService userService;

    @RequestMapping("login.do")
    @ResponseBody
    public ServerResponse login(String telphone, String password, HttpSession session){
        if(telphone == null || password == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        ServerResponse serverResponse = userService.login(telphone,password);

        if(serverResponse.isSuccess()){
            UserLoginInfo userLoginInfo = (UserLoginInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,userLoginInfo);
            SessionListener.sessionHashMap.put(userLoginInfo.getUserID(),session);
        }
        return serverResponse;
    }

    @RequestMapping("logout.do")
    @ResponseBody
    public ServerResponse logout(HttpSession session){
        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(Const.CURRENT_USER);
        if(userLoginInfo == null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        session.removeAttribute(Const.CURRENT_USER);
        SessionListener.sessionHashMap.remove(userLoginInfo.getUserID());
        return ServerResponse.createBySuccess();
    }

    @RequestMapping("registered_check_telphone.do")
    @ResponseBody
    public ServerResponse registeredCheckTelphone(String telphone){

        return userService.registered_checkTelphone(telphone);
    }

    @RequestMapping("check_telphone.do")
    @ResponseBody
    public ServerResponse CheckTelphone(String telphone){

        return userService.checkTelphoneReg(telphone);
    }

    @RequestMapping("registered_check_code.do")
    @ResponseBody
    public ServerResponse registeredCheckCode(String code,String telphone,String password,String hxUserName){

        return userService.registered_checkCode(code,telphone,password,hxUserName);
    }


}
