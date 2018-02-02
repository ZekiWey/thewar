package com.lw.pojo;

import java.util.Date;

public class UserLoginPhone {
    private Integer id;

    private String telphone;

    private String password;

    private Integer pluginsId;

    private Date createTime;

    public UserLoginPhone(Integer id, String telphone, String password, Integer pluginsId, Date createTime) {
        this.id = id;
        this.telphone = telphone;
        this.password = password;
        this.pluginsId = pluginsId;
        this.createTime = createTime;
    }

    public UserLoginPhone() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getPluginsId() {
        return pluginsId;
    }

    public void setPluginsId(Integer pluginsId) {
        this.pluginsId = pluginsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}