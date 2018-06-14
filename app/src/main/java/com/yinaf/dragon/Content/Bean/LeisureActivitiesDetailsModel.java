package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;

public class LeisureActivitiesDetailsModel implements Serializable{

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"actContent":"","actId":1,"actIntegral":5,"actPlaces":50,"actSummary":"春眠不觉晓，健康知多少。别等春天结束了才发现身\r\n体没养好。","activityImg":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg","activityTitle":"活动啊","address":"东街口","area":"鼓楼区","city":"福州市","createTime":"","dataFlag":0,"endTime":"2018-04-27","province":"福建省","sponsorName":"福建颐纳福智能养老产业发展有限公司","sponsorTelephone":"微信群548546512,QQ群54125654","startTime":"2018-04-06","updateTime":"","usePlaces":20}
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
         * actContent :
         * actId : 1
         * actIntegral : 5
         * actPlaces : 50
         * actSummary : 春眠不觉晓，健康知多少。别等春天结束了才发现身
         体没养好。
         * activityImg : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg
         * activityTitle : 活动啊
         * address : 东街口
         * area : 鼓楼区
         * city : 福州市
         * createTime :
         * dataFlag : 0
         * endTime : 2018-04-27
         * province : 福建省
         * sponsorName : 福建颐纳福智能养老产业发展有限公司
         * sponsorTelephone : 微信群548546512,QQ群54125654
         * startTime : 2018-04-06
         * updateTime :
         * usePlaces : 20
         */

        private String actContent;
        private String actId;
        private String actIntegral;
        private String actPlaces;
        private String actSummary;
        private String activityImg;
        private String activityTitle;
        private String address;
        private String area;
        private String city;
        private String createTime;
        private String dataFlag;
        private String endTime;
        private String province;
        private String sponsorName;
        private String sponsorTelephone;
        private String startTime;
        private String updateTime;
        private String usePlaces;

        public String getActContent() {
            return actContent;
        }

        public void setActContent(String actContent) {
            this.actContent = actContent;
        }

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        public String getActIntegral() {
            return actIntegral;
        }

        public void setActIntegral(String actIntegral) {
            this.actIntegral = actIntegral;
        }

        public String getActPlaces() {
            return actPlaces;
        }

        public void setActPlaces(String actPlaces) {
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

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDataFlag() {
            return dataFlag;
        }

        public void setDataFlag(String dataFlag) {
            this.dataFlag = dataFlag;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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

        public String getUsePlaces() {
            return usePlaces;
        }

        public void setUsePlaces(String usePlaces) {
            this.usePlaces = usePlaces;
        }
    }
}
