package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018/05/09.
 * 功能：成员积分日志的实体类
 */

public class MembersIntegralLog {

    public int logId;//日志ID

    public int logType;//日志状态 ( 1 输入积分  2 输入积分)

    public int memIntegral;//成员积分

    public String reminder;//提示语

    public int memberId;//成员ID

    public String createTime;//创建时间

    public int type;//类型

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public int getMemIntegral() {
        return memIntegral;
    }

    public void setMemIntegral(int memIntegral) {
        this.memIntegral = memIntegral;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
