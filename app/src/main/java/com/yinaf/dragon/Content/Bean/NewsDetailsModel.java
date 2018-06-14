package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;

/**
 * 资讯详情
 */
public class NewsDetailsModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"new_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291822334551.jpg","update_time":"2018-03-29 18:31:10","new_content":"是打发斯蒂芬","new_title":"是打发斯蒂芬","new_id":114}
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
         * new_pic : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291822334551.jpg
         * update_time : 2018-03-29 18:31:10
         * new_content : 是打发斯蒂芬
         * new_title : 是打发斯蒂芬
         * new_id : 114
         */

        private String new_pic;
        private String update_time;
        private String new_content;
        private String new_title;
        private int new_id;

        public String getNew_pic() {
            return new_pic;
        }

        public void setNew_pic(String new_pic) {
            this.new_pic = new_pic;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getNew_content() {
            return new_content;
        }

        public void setNew_content(String new_content) {
            this.new_content = new_content;
        }

        public String getNew_title() {
            return new_title;
        }

        public void setNew_title(String new_title) {
            this.new_title = new_title;
        }

        public int getNew_id() {
            return new_id;
        }

        public void setNew_id(int new_id) {
            this.new_id = new_id;
        }
    }
}
