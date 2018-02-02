package com.lw.user.service.impl;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.lw.common.*;
import com.lw.pojo.UserLoginPhone;
import com.lw.pojo.UserPlugins;
import com.lw.user.dao.UserLoginPhoneMapper;
import com.lw.user.dao.UserPluginsMapper;
import com.lw.user.service.IUserService;
import com.lw.vo.UserLoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2018/1/2.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserLoginPhoneMapper userLoginPhoneMapper;

    @Autowired
    private UserPluginsMapper userPluginsMapper;

    //验证登录
    public ServerResponse login(String telphone, String password){

        if(!checkTelphone(telphone)){
            return ServerResponse.createByErrorMessage("该用户不存在");
        }
        //todo 实现密码的md5加密
        UserLoginPhone userLoginPhone = userLoginPhoneMapper.selectcheckUserLogin(telphone,password);

        if(userLoginPhone == null){
            return ServerResponse.createByErrorMessage("密码输入错误");
        }

        if(null != SessionListener.sessionHashMap.get(userLoginPhone.getPluginsId())){
            return ServerResponse.createByErrorMessage("该用户已经在一个设备登录");
        }

        UserPlugins userPlugins = userPluginsMapper.selectByPrimaryKey(userLoginPhone.getPluginsId());

        UserLoginInfo userLoginInfo = new UserLoginInfo();

        userLoginInfo.setHxUserName(userPlugins.getHxUsername());
        userLoginInfo.setMapUserName(userPlugins.getMapUsername());
        userLoginInfo.setLoginStatus(userPlugins.getLoginStatus());
        userLoginInfo.setLoginWay(Const.LoginWay.PHONE_LOGIN.getCode());
        userLoginInfo.setLoginAccount(userLoginPhone.getTelphone());
        userLoginInfo.setUserID(userLoginPhone.getPluginsId());

        return ServerResponse.createBySuccess(userLoginInfo);
    }



    //校验手机号的唯一性
    public ServerResponse checkTelphoneUniq(String telphone){
        if(checkTelphone(telphone)){
            return ServerResponse.createByErrorMessage("该手机号已被注册");
        }
        return ServerResponse.createBySuccess();
    }


    //验证手机号并发送验证码
    public ServerResponse registered_checkTelphone(String telphone){

        if(checkTelphone(telphone)){
            return ServerResponse.createByErrorMessage("该手机号已被注册");
        }

        String code = sendSmsCode(telphone);

        if(code == null){
            return ServerResponse.createByErrorMessage("验证码获取失败，请重试");
        }

        CodeCache.setKey(CodeCache.TOKEN_PREFIX+telphone,code);

        String hxUserName = getHXUserName();


        return ServerResponse.createBySuccess(hxUserName);

    }

    //验证输入的验证码是否正确
    public ServerResponse registered_checkCode(String code,String telphone,String password,String hxUserName){
        if(code == null || telphone == null || password == null || hxUserName == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }


        if(code.equals(CodeCache.getKey(CodeCache.TOKEN_PREFIX+telphone))){
            UserPlugins userPlugins = new UserPlugins();
            userPlugins.setHxUsername(hxUserName);
            userPlugins.setMapUsername(hxUserName);
            userPlugins.setLoginStatus(0);
            int result = userPluginsMapper.insert(userPlugins);
            if(result <= 0){
                return ServerResponse.createByErrorMessage("注册失败");
            }
            UserLoginPhone user = new UserLoginPhone();
            user.setTelphone(telphone);
            user.setPassword(password);
            user.setPluginsId(userPlugins.getId());
            result =userLoginPhoneMapper.insert(user);

            if(result > 0){
                return ServerResponse.createBySuccessMsg("注册成功");
            }
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createByErrorMessage("验证码已过期或者不正确");
    }



    //发送验证码
    public String sendSmsCode(String telphone){
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名
        //AK
        final String accessKeyId = "LTAIZYOXbIOCScdg";//accessKeyId
        final String accessKeySecret = "Rk8mVQ24ipWdTyYHsJVki4Zfr8PN1y";//accessKeySecret
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        //手机号
        request.setPhoneNumbers(telphone);
        //签名
        request.setSignName("于海超");
        //模版
        request.setTemplateCode("SMS_121161051");
        //生成验证码
        int ran = (int) (Math.random() * 9999 + 1);
        String code = String.valueOf(ran);
        //替换Json串
        request.setTemplateParam("{\"code\":\""+ code + "\"}");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        System.out.println(sendSmsResponse.getCode());
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            System.out.println("发送成功");
            return code;
        }
        return null;
    }



    public ServerResponse checkTelphoneReg(String telphone){
        if(checkTelphone(telphone)){
            return ServerResponse.createByErrorMessage("该手机号已被注册");
        }
        return ServerResponse.createBySuccess();
    }







    private Boolean checkTelphone(String telphone){
        int result = userLoginPhoneMapper.selectCheckTelphone(telphone);
        return result > 0;
    }




    public String getHXUserName(){
        String hxUserName = getUUID();
        System.out.println(userPluginsMapper.selectHXUsername(hxUserName));
        while (userPluginsMapper.selectHXUsername(hxUserName) > 0){
            hxUserName=getUUID();
        }
        return hxUserName;
    }

    public  String getUUID() {
        String chars = "qwertyuioplkjhgfdsazxcvbnm";
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = chars.charAt(new Random().nextInt(26)) + str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 17);
        return temp;
    }


}
