package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;
import java.util.List;

/**
 * 药物提醒
 */
public class DrugSetModel implements Serializable{
    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"createTime":"2018-03-02 11:34:26","endTime":"2018-03-18","medicineId":1,"medicineRemarks":"不提醒吧1","medicineTitle":"提醒啊啊","memberId":1,"remindTime":"12:36","startTime":"2018-03-13","updateTime":"2018-03-02 11:46:47","watchId":1},{"createTime":"2018-03-02 11:50:52","endTime":"2018-03-23","medicineId":2,"medicineRemarks":"不提醒吧2","medicineTitle":"提醒啊2","memberId":1,"remindTime":"13:35","startTime":"2018-03-02","updateTime":"2018-03-06 14:47:31","watchId":1},{"createTime":"2018-03-02 19:56:19","endTime":"2018-03-31","medicineId":3,"medicineRemarks":"不提醒吧dfdfdf","medicineTitle":"提醒啊啊dffd","memberId":1,"remindTime":"12:36","startTime":"2018-03-20","updateTime":"2018-03-06 14:47:33","watchId":1}]
     * success : true
     */

    private String code;
    private String msg;
    private boolean success;
    private List<ObjBean> obj;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * createTime : 2018-03-02 11:34:26
         * endTime : 2018-03-18
         * medicineId : 1
         * medicineRemarks : 不提醒吧1
         * medicineTitle : 提醒啊啊
         * memberId : 1
         * remindTime : 12:36
         * startTime : 2018-03-13
         * updateTime : 2018-03-02 11:46:47
         * watchId : 1
         */

        private String createTime;
        private String endTime;
        private int medicineId;
        private String medicineRemarks;
        private String medicineTitle;
        private int memberId;
        private String remindTime;
        private String startTime;
        private String updateTime;
        private int watchId;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(int medicineId) {
            this.medicineId = medicineId;
        }

        public String getMedicineRemarks() {
            return medicineRemarks;
        }

        public void setMedicineRemarks(String medicineRemarks) {
            this.medicineRemarks = medicineRemarks;
        }

        public String getMedicineTitle() {
            return medicineTitle;
        }

        public void setMedicineTitle(String medicineTitle) {
            this.medicineTitle = medicineTitle;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getWatchId() {
            return watchId;
        }

        public void setWatchId(int watchId) {
            this.watchId = watchId;
        }
    }
}
