package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;
import java.util.List;

/**
 * 地址
 */
public class AddressSetModel implements Serializable{
    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"address":"海西佰悦城2期","addressId":2,"createTime":"2018-03-04 11:12:47","defcode":0,"lat":"","lon":"","memberId":1,"phone":"18955666777","receiver":"李白","region":"福建省-福州市-仓山区","updateTime":"2018-03-04 11:12:47","zipcode":"362800"},{"address":"海西佰悦城2期","addressId":3,"createTime":"2018-03-04 11:12:57","defcode":0,"lat":"","lon":"","memberId":1,"phone":"18955222777","receiver":"杜甫","region":"福建省-福州市-仓山区","updateTime":"2018-03-04 11:12:57","zipcode":"362800"},{"address":"海西佰悦城2期","addressId":4,"createTime":"2018-03-04 11:13:04","defcode":0,"lat":"","lon":"","memberId":1,"phone":"18955222777","receiver":"白居易","region":"福建省-福州市-仓山区","updateTime":"2018-03-04 11:13:04","zipcode":"362800"}]
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
         * address : 海西佰悦城2期
         * addressId : 2
         * createTime : 2018-03-04 11:12:47
         * defcode : 0
         * lat :
         * lon :
         * memberId : 1
         * phone : 18955666777
         * receiver : 李白
         * region : 福建省-福州市-仓山区
         * updateTime : 2018-03-04 11:12:47
         * zipcode : 362800
         */

        private String address;
        private int addressId;
        private String createTime;
        private int defcode;
        private String lat;
        private String lon;
        private int memberId;
        private String phone;
        private String receiver;
        private String region;
        private String updateTime;
        private String zipcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDefcode() {
            return defcode;
        }

        public void setDefcode(int defcode) {
            this.defcode = defcode;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
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

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }
}
