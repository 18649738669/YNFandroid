package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

/**
 * 发送频率
 */
public class FrequencySetModel implements Serializable{

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"position":600,"bloodSuger":600,"bloodPressure":600,"heartRate":600,"memberId":1,"watchId":1}
     * success : true
     */

    private String code;
    private String msg;
    private ObjBean obj;
    private boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ObjBean {
        /**
         * position : 600
         * bloodSuger : 600
         * bloodPressure : 600
         * heartRate : 600
         * memberId : 1
         * watchId : 1
         */

        private String position;
        private String bloodSuger;
        private String bloodPressure;
        private String heartRate;
        private String memberId;
        private String watchId;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getBloodSuger() {
            return bloodSuger;
        }

        public void setBloodSuger(String bloodSuger) {
            this.bloodSuger = bloodSuger;
        }

        public String getBloodPressure() {
            return bloodPressure;
        }

        public void setBloodPressure(String bloodPressure) {
            this.bloodPressure = bloodPressure;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(String heartRate) {
            this.heartRate = heartRate;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getWatchId() {
            return watchId;
        }

        public void setWatchId(String watchId) {
            this.watchId = watchId;
        }
    }
}
