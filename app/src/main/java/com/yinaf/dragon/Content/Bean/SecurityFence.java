package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018/05/04.
 * 功能：安全围栏实体类
 */

public class SecurityFence {

    public int railId;//围栏ID

    public int watchId;//腕表ID

    public int radius;//有效半径

    public String name;//名称

    public String latitude;//纬度

    public String longitude;//经度

    public String address;//安全区域地址

    public String deviceTime;//时间

    public String lastAction;//最后一次动作( 进入:enter 离开:leave)

    public int getRailId() {
        return railId;
    }

    public void setRailId(int railId) {
        this.railId = railId;
    }

    public int getWatchId() {
        return watchId;
    }

    public void setWatchId(int watchId) {
        this.watchId = watchId;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
}
