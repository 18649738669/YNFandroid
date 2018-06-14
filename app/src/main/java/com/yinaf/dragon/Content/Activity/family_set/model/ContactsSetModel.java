package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人
 */
public class ContactsSetModel implements Serializable {
    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"address":"上海市浦东区","contactsId":3,"memberId":1,"phone":"18955333666","rela":"母子","trueName":"赵志伟"}]
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
        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAreas() {
            return areas;
        }

        public void setAreas(String areas) {
            this.areas = areas;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        /**
         * address : 上海市浦东区
         * contactsId : 3
         * memberId : 1
         * phone : 18955333666
         * rela : 母子
         * trueName : 赵志伟
         */
        private String city;
        private String areas;
        private String province;
        private String address;
        private int contactsId;
        private int memberId;
        private String phone;
        private String rela;
        private String trueName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getContactsId() {
            return contactsId;
        }

        public void setContactsId(int contactsId) {
            this.contactsId = contactsId;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRela() {
            return rela;
        }

        public void setRela(String rela) {
            this.rela = rela;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
    }
}
