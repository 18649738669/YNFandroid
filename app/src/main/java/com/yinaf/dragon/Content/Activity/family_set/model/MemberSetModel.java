package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

/**
 * 成员基本资料
 */
public class MemberSetModel implements Serializable{

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"address":"福建省福州市仓山区海西佰悦城2期","birthday":"1949-08-02","blood":"B型","createTime":"2018-01-11 17:18:26.0","dataFlag":1,"distance":"同城","domicile":"12","education":"本科","height":"170","householdRegister":"1","identity":"350105197912121235","image":"http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/img/1802071741075397_tmp.jpg","income":"5000000","isLead":0,"job":"退休","liveState":"疗养机构","marriage":"已婚","memberId":1,"memberNum":"123456789","nation":"汉族","occupation":"金融/银行/投资/保险","phone":"15080091234","realName":"昵称吗","sex":1,"telephone":"0591-83687121","trueName":"陈某某","unit":"什么银行","updateTime":"2018-02-27 10:53:34.0","weight":"62"}
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
         * address : 福建省福州市仓山区海西佰悦城2期
         * birthday : 1949-08-02
         * blood : B型
         * createTime : 2018-01-11 17:18:26.0
         * dataFlag : 1
         * distance : 同城
         * domicile : 12
         * education : 本科
         * height : 170
         * householdRegister : 1
         * identity : 350105197912121235
         * image : http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/img/1802071741075397_tmp.jpg
         * income : 5000000
         * isLead : 0
         * job : 退休
         * liveState : 疗养机构
         * marriage : 已婚
         * memberId : 1
         * memberNum : 123456789
         * nation : 汉族
         * occupation : 金融/银行/投资/保险
         * phone : 15080091234
         * realName : 昵称吗
         * sex : 1
         * telephone : 0591-83687121
         * trueName : 陈某某
         * unit : 什么银行
         * updateTime : 2018-02-27 10:53:34.0
         * weight : 62
         */

        private String address;
        private String birthday;
        private String blood;
        private String createTime;
        private int dataFlag;
        private String distance;
        private String domicile;
        private String education;
        private String height;
        private String householdRegister;
        private String identity;
        private String image;
        private String income;
        private int isLead;
        private String job;
        private String liveState;
        private String marriage;
        private int memberId;
        private String memberNum;
        private String nation;
        private String occupation;
        private String phone;

        public String getRela() {
            return rela;
        }

        public void setRela(String rela) {
            this.rela = rela;
        }

        private String rela;
        private String realName;
        private int sex;
        private String telephone;
        private String trueName;
        private String unit;
        private String updateTime;
        private String weight;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBlood() {
            return blood;
        }

        public void setBlood(String blood) {
            this.blood = blood;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDomicile() {
            return domicile;
        }

        public void setDomicile(String domicile) {
            this.domicile = domicile;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getHouseholdRegister() {
            return householdRegister;
        }

        public void setHouseholdRegister(String householdRegister) {
            this.householdRegister = householdRegister;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public int getIsLead() {
            return isLead;
        }

        public void setIsLead(int isLead) {
            this.isLead = isLead;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getLiveState() {
            return liveState;
        }

        public void setLiveState(String liveState) {
            this.liveState = liveState;
        }

        public String getMarriage() {
            return marriage;
        }

        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
