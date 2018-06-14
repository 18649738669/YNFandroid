package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018-4-17.
 * 功能：成员的实体类
 */

public class Member {


    /**
     *成员主键 id
     */
    private int memberId;
    /**
     *腕表id(如为空则未绑定腕表)
     */
    private int watchId;
    /**
     * 成员编号
     */
    private String memberNum;
    /**
     * 地址
     */
    private String address;
    /**
     * 与用户关系
     */
    private String rela;
    /**
     * 性别 （ 0未选择  1男 2女 ）
     */
    private int sex;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 血型
     */
    private String blood;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 与子女距离
     */
    private String distance;
    /**
     * 所在地
     */
    private String domicile;
    /**
     * 学历
     */
    private String education;
    /**
     * 户籍地
     */
    private String householdRegister;
    /**
     * 身高（单位 cm）
     */
    private String height;
    /**
     * 身份证
     */
    private String identity;
    /**
     * 头像
     */
    private String image;
    /**
     * 收入情况(单位 元)
     */
    private String income;
    /**
     * 工作情况
     */
    private String job;
    /**
     * 居住情况
     */
    private String liveState;
    /**
     * 婚姻状况
     */
    private String marriage;
    /**
     * 民族
     */
    private String nation;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 手机
     */
    private String phone;
    /**
     * 昵称
     */
    private String realName;
    /**
     * 固定电话
     */
    private String telephone;
    /**
     * 真实姓名
     */
    private String trueName;
    /**
     * 工作单位
     */
    private String unit;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 体重（单位 kg）
     */
    private String weight;
    /**
     * 是否主副（0副 1主）
     */
    private int isLead;

    @Override
    public String toString() {
        return "Member{"+
                "memberId='" + memberId + '\'' +
                "watchId='" + watchId + '\'' +
                "memberNum='" + memberNum + '\'' +
                "address='" + address + '\'' +
                "rela='" + rela + '\'' +
                "sex='" + sex + '\'' +
                "birthday='" + birthday + '\'' +
                "blood='" + blood + '\'' +
                "createTime='" + createTime + '\'' +
                "distance='" + distance + '\'' +
                "domicile='" + domicile + '\'' +
                "education='" + education + '\'' +
                "householdRegister='" + householdRegister + '\'' +
                "height='" + height + '\'' +
                "identity='" + identity + '\'' +
                "image='" + image + '\'' +
                "income='" + income + '\'' +
                "job='" + job + '\'' +
                "liveState='" + liveState + '\'' +
                "marriage='" + marriage + '\'' +
                "nation='" + nation + '\'' +
                "occupation='" + occupation + '\'' +
                "phone='" + phone + '\'' +
                "realName='" + realName + '\'' +
                "telephone='" + telephone + '\'' +
                "trueName='" + trueName + '\'' +
                "unit='" + unit + '\'' +
                "updateTime='" + updateTime + '\'' +
                "weight='" + weight + '\'' +
                "isLead='" + isLead + '\'' +
                "}";
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

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRela() {
        return rela;
    }

    public void setRela(String rela) {
        this.rela = rela;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getHouseholdRegister() {
        return householdRegister;
    }

    public void setHouseholdRegister(String householdRegister) {
        this.householdRegister = householdRegister;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public int getIsLead() {
        return isLead;
    }

    public void setIsLead(int isLead) {
        this.isLead = isLead;
    }
}
