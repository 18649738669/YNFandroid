package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 地址
 */
public class MyAddressSetModel implements Serializable{
    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"address":"海西佰悦城2期444","auId":30,"createTime":"2018-05-02 10:07:01","defcode":0,"phone":"18955222777","receiver":"李白555555","region":"福建省-福州市-仓山区","updateTime":"2018-05-02 10:12:06","userId":1,"zipcode":"362800"},{"address":"78956232335","auId":29,"createTime":"2018-04-29 18:49:05","defcode":0,"phone":"15635626986","receiver":"呵呵呵","region":"北京市-市辖区-东城区","updateTime":"2018-04-29 12:13:06","userId":1,"zipcode":"685922"},{"address":"地址","auId":28,"createTime":"2018-04-27 22:59:08","defcode":0,"phone":"13654568489","receiver":"地址","region":"北京市-市辖区-东城区","updateTime":"2018-04-29 10:50:33","userId":1,"zipcode":"6532650"}]
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

    public static class ObjBean implements Serializable {
        /**
         * address : 海西佰悦城2期444
         * auId : 30
         * createTime : 2018-05-02 10:07:01
         * defcode : 0
         * phone : 18955222777
         * receiver : 李白555555
         * region : 福建省-福州市-仓山区
         * updateTime : 2018-05-02 10:12:06
         * userId : 1
         * zipcode : 362800
         */

        private String address;
        private int auId;
        private String createTime;
        private int defcode;
        private String phone;
        private String receiver;
        private String region;
        private String updateTime;
        private int userId;
        private String zipcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAuId() {
            return auId;
        }

        public void setAuId(int auId) {
            this.auId = auId;
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }
}
