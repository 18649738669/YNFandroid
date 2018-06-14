package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;
import java.util.List;

/**
 * 关联 App
 */
public class AppSetModel implements Serializable{
    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"userState":1,"domicile":"福建福州仓山","phone":"18966333555","sex":1,"rela":"父子","relaId":1,"userId":1,"image":"http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/test/3j001.jpg","relaName":"会飞的鱼","userType":0},{"userState":1,"domicile":"福建福州台江1","phone":"18966333555","sex":2,"rela":"女儿","relaId":3,"userId":2,"image":"http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/test/3j001.jpg","relaName":"齐天大圣","userType":0}]
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

    public static class ObjBean {
        /**
         * userState : 1
         * domicile : 福建福州仓山
         * phone : 18966333555
         * sex : 1
         * rela : 父子
         * relaId : 1
         * userId : 1
         * image : http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/test/3j001.jpg
         * relaName : 会飞的鱼
         * userType : 0
         */

        private int userState;
        private String domicile;
        private String phone;
        private int sex;
        private String rela;
        private int relaId;
        private int userId;
        private String image;
        private String relaName;
        private int userType;

        public int getUserState() {
            return userState;
        }

        public void setUserState(int userState) {
            this.userState = userState;
        }

        public String getDomicile() {
            return domicile;
        }

        public void setDomicile(String domicile) {
            this.domicile = domicile;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getRela() {
            return rela;
        }

        public void setRela(String rela) {
            this.rela = rela;
        }

        public int getRelaId() {
            return relaId;
        }

        public void setRelaId(int relaId) {
            this.relaId = relaId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRelaName() {
            return relaName;
        }

        public void setRelaName(String relaName) {
            this.relaName = relaName;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
