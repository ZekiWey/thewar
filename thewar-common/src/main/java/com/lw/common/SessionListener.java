package com.lw.common;

import com.google.common.collect.Maps;
import com.lw.vo.UserLoginInfo;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/3.
 */
public class SessionListener implements HttpSessionListener {

    public static HashMap<Integer,HttpSession> sessionHashMap =Maps.newHashMap();


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        delSession(session);
    }

    public static synchronized void delSession(HttpSession session)
    {
        if(session != null)
        {
            // 删除单一登录中记录的变量
            if(session.getAttribute(Const.CURRENT_USER) != null)
            {
                UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(Const.CURRENT_USER);
                SessionListener.sessionHashMap.remove( userLoginInfo.getUserID());
            }
        }
    }
}
