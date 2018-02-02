package com.lw.common;

/**
 * Created by Administrator on 2018/1/2.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public enum LoginWay{

        PHONE_LOGIN(1,"手机"),
        WX_LOGIN(2,"微信"),
        QQ_LOGIN(3,"QQ");

        LoginWay(int code,String value){
            this.code = code;
            this.value = value;
        }
        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

}
