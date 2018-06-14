package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;

public class MemberActivitiesDetailsModel implements Serializable{

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"actAddress":"福建省福州市鼓楼区东街口","actContent":"","actId":1,"actIntegral":5,"actPlaces":50,"actSummary":"春眠不觉晓，健康知多少。别等春天结束了才发现身\r\n体没养好。","activityImg":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg","activityNum":"","activityTitle":"活动啊","createTime":"","dataFlag":0,"endTime":"2018-04-25","maId":0,"memberId":0,"memberName":"我修改了名称","sponsorName":"福建颐纳福智能养老产业发展有限公司","sponsorTelephone":"微信群548546512,QQ群54125654","startTime":"2018-04-13","updateTime":"","userId":0,"userName":"18966333555"}
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
         * actAddress : 福建省福州市鼓楼区东街口
         * actContent :
         * actId : 1
         * actIntegral : 5
         * actPlaces : 50
         * actSummary : 春眠不觉晓，健康知多少。别等春天结束了才发现身
         体没养好。
         * activityImg : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg
         * activityNum :
         * activityTitle : 活动啊
         * createTime :
         * dataFlag : 0
         * endTime : 2018-04-25
         * maId : 0
         * memberId : 0
         * memberName : 我修改了名称
         * sponsorName : 福建颐纳福智能养老产业发展有限公司
         * sponsorTelephone : 微信群548546512,QQ群54125654
         * startTime : 2018-04-13
         * updateTime :
         * userId : 0
         * userName : 18966333555
         */

        private String actAddress;
        private String actContent;
        private int actId;
        private int actIntegral;
        private int actPlaces;
        private String actSummary;
        private String activityImg;
        private String activityNum;
        private String activityTitle;
        private String createTime;
        private int dataFlag;
        private String endTime;
        private int maId;
        private int memberId;
        private String memberName;
        private String sponsorName;
        private String sponsorTelephone;
        private String startTime;
        private String updateTime;
        private int userId;
        private String userName;

        public String getActAddress() {
            return actAddress;
        }

        public void setActAddress(String actAddress) {
            this.actAddress = actAddress;
        }

        public String getActContent() {
            return actContent;
        }

        public void setActContent(String actContent) {
            this.actContent = actContent;
        }

        public int getActId() {
            return actId;
        }

        public void setActId(int actId) {
            this.actId = actId;
        }

        public int getActIntegral() {
            return actIntegral;
        }

        public void setActIntegral(int actIntegral) {
            this.actIntegral = actIntegral;
        }

        public int getActPlaces() {
            return actPlaces;
        }

        public void setActPlaces(int actPlaces) {
            this.actPlaces = actPlaces;
        }

        public String getActSummary() {
            return actSummary;
        }

        public void setActSummary(String actSummary) {
            this.actSummary = actSummary;
        }

        public String getActivityImg() {
            return activityImg;
        }

        public void setActivityImg(String activityImg) {
            this.activityImg = activityImg;
        }

        public String getActivityNum() {
            return activityNum;
        }

        public void setActivityNum(String activityNum) {
            this.activityNum = activityNum;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDataFlag() {
            return dataFlag;
        }

        public void setDataFlag(int dataFlag) {
            this.dataFlag = dataFlag;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getMaId() {
            return maId;
        }

        public void setMaId(int maId) {
            this.maId = maId;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getSponsorName() {
            return sponsorName;
        }

        public void setSponsorName(String sponsorName) {
            this.sponsorName = sponsorName;
        }

        public String getSponsorTelephone() {
            return sponsorTelephone;
        }

        public void setSponsorTelephone(String sponsorTelephone) {
            this.sponsorTelephone = sponsorTelephone;
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
