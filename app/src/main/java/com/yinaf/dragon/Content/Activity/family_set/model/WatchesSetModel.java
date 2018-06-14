package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

public class WatchesSetModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"phone":"15518786885","seriesName":"9系列","watchImei":"888821000012415","openTime":"","watchId":1}
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
         * phone : 15518786885
         * seriesName : 9系列
         * watchImei : 888821000012415
         * openTime :
         * watchId : 1
         */

        private String phone;
        private String seriesName;
        private String watchImei;
        private String openTime;
        private int watchId;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getWatchImei() {
            return watchImei;
        }

        public void setWatchImei(String watchImei) {
            this.watchImei = watchImei;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public int getWatchId() {
            return watchId;
        }

        public void setWatchId(int watchId) {
            this.watchId = watchId;
        }
    }
}
