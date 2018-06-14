package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;
import java.util.List;

/**
 * 通讯录
 */
public class AddressBookSetModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"address":"海西佰悦城2号楼","areas":"台江区","bookId":48,"city":"福州市","memberId":2,"phone":"15080090142","province":"福建省","rela":"父亲","trueName":"陈天才"}]
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
         * address : 海西佰悦城2号楼
         * areas : 台江区
         * bookId : 48
         * city : 福州市
         * memberId : 2
         * phone : 15080090142
         * province : 福建省
         * rela : 父亲
         * trueName : 陈天才
         */

        private String address;
        private String areas;
        private int bookId;
        private String city;
        private int memberId;
        private String phone;
        private String province;
        private String rela;
        private String trueName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreas() {
            return areas;
        }

        public void setAreas(String areas) {
            this.areas = areas;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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
