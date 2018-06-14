package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018/05/22.
 * 功能：智能门锁的实体类
 */

public class DoorLock {

    String deviceid;//网关ID（序列号）

    String subname;//设备名称

    String subid;//门锁序列号


    String signtime;//设备建立时间

    public DoorLock(String deviceid, String subname, String subid, String signtime) {
        this.deviceid = deviceid;
        this.subname = subname;
        this.subid = subid;
        this.signtime = signtime;
    }

    public DoorLock() {
    }


    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getSigntime() {
        return signtime;
    }

    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }
}
