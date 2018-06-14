package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

/**
 * 步长设置
 */
public class FootstepSetModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"runStep":40,"memberId":1,"watchId":1,"walkStep":30}
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
         * runStep : 40
         * memberId : 1
         * watchId : 1
         * walkStep : 30
         */

        private int runStep;
        private int memberId;
        private int watchId;
        private int walkStep;

        public int getRunStep() {
            return runStep;
        }

        public void setRunStep(int runStep) {
            this.runStep = runStep;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public int getWatchId() {
            return watchId;
        }

        public void setWatchId(int watchId) {
            this.watchId = watchId;
        }

        public int getWalkStep() {
            return walkStep;
        }

        public void setWalkStep(int walkStep) {
            this.walkStep = walkStep;
        }
    }
}
