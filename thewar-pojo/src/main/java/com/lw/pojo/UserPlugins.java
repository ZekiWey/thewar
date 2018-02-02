package com.lw.pojo;

import java.util.Date;

public class UserPlugins {
    private Integer id;

    private String hxUsername;

    private String mapUsername;

    private Integer loginStatus;

    private Date createTime;

    public UserPlugins(Integer id, String hxUsername, String mapUsername, Integer loginStatus, Date createTime) {
        this.id = id;
        this.hxUsername = hxUsername;
        this.mapUsername = mapUsername;
        this.loginStatus = loginStatus;
        this.createTime = createTime;
    }

    public UserPlugins() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHxUsername() {
        return hxUsername;
    }

    public void setHxUsername(String hxUsername) {
        this.hxUsername = hxUsername == null ? null : hxUsername.trim();
    }

    public String getMapUsername() {
        return mapUsername;
    }

    public void setMapUsername(String mapUsername) {
        this.mapUsername = mapUsername == null ? null : mapUsername.trim();
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}