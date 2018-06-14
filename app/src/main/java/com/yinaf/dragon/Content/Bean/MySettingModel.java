package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;

/**
 * 用户基本信息
 */
public class MySettingModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"birthday":"2018-01-09","blood":"A型","createTime":"2018-01-09 10:36:36","dataFlag":1,"domicile":"福建福州仓山","householdRegister":"福建福州仓山","image":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/0209/1803020919227208.jpg","mail":"123456789@qq.com","occupation":"学生","passWord":"","phone":"18966333555","realName":"会飞的鱼","sex":1,"updateTime":"2018-03-07 11:35:39","userId":1,"userName":"18966333555","userState":1,"userType":0}
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

    public  ObjBean getObj() {
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

    public static class ObjBean implements Serializable{
        /**
         * birthday : 2018-01-09
         * blood : A型
         * createTime : 2018-01-09 10:36:36
         * dataFlag : 1
         * domicile : 福建福州仓山
         * householdRegister : 福建福州仓山
         * image : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/0209/1803020919227208.jpg
         * mail : 123456789@qq.com
         * occupation : 学生
         * passWord :
         * phone : 18966333555
         * realName : 会飞的鱼
         * sex : 1
         * updateTime : 2018-03-07 11:35:39
         * userId : 1
         * userName : 18966333555
         * userState : 1
         * userType : 0
         */

        private String birthday;
        private String blood;
        private String createTime;
        private int dataFlag;
        private String domicile;
        private String householdRegister;
        private String image;
        private String mail;
        private String occupation;
        private String passWord;
        private String phone;
        private String realName;
        private int sex;
        private String updateTime;
        private int userId;
        private String userName;
        private int userState;
        private int userType;

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

        public String getDomicile() {
            return domicile;
        }

        public void setDomicile(String domicile) {
            this.domicile = domicile;
        }

        public String getHouseholdRegister() {
            return householdRegister;
        }

        public void setHouseholdRegister(String householdRegister) {
            this.householdRegister = householdRegister;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
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

        public int getUserState() {
            return userState;
        }

        public void setUserState(int userState) {
            this.userState = userState;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
