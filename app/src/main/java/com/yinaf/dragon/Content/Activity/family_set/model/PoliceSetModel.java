package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

/**
 * 预警提醒
 */
public class PoliceSetModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"sbpMin":90,"hrMax":90,"sbpMax":140,"gluMax":6.1,"dbpMin":60,"memberId":1,"watchId":1,"dbpMax":90,"gluMin":3.9,"hrMin":60}
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
         * sbpMin : 90
         * hrMax : 90
         * sbpMax : 140
         * gluMax : 6.1
         * dbpMin : 60
         * memberId : 1
         * watchId : 1
         * dbpMax : 90
         * gluMin : 3.9
         * hrMin : 60
         */

        private String sbpMin;
        private String hrMax;
        private String sbpMax;
        private String gluMax;
        private String dbpMin;
        private String memberId;
        private String watchId;
        private String dbpMax;
        private String gluMin;
        private String hrMin;

        public String getSbpMin() {
            return sbpMin;
        }

        public void setSbpMin(String sbpMin) {
            this.sbpMin = sbpMin;
        }

        public String getHrMax() {
            return hrMax;
        }

        public void setHrMax(String hrMax) {
            this.hrMax = hrMax;
        }

        public String getSbpMax() {
            return sbpMax;
        }

        public void setSbpMax(String sbpMax) {
            this.sbpMax = sbpMax;
        }

        public String getGluMax() {
            return gluMax;
        }

        public void setGluMax(String gluMax) {
            this.gluMax = gluMax;
        }

        public String getDbpMin() {
            return dbpMin;
        }

        public void setDbpMin(String dbpMin) {
            this.dbpMin = dbpMin;
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

        public String getDbpMax() {
            return dbpMax;
        }

        public void setDbpMax(String dbpMax) {
            this.dbpMax = dbpMax;
        }

        public String getGluMin() {
            return gluMin;
        }

        public void setGluMin(String gluMin) {
            this.gluMin = gluMin;
        }

        public String getHrMin() {
            return hrMin;
        }

        public void setHrMin(String hrMin) {
            this.hrMin = hrMin;
        }
    }
}
