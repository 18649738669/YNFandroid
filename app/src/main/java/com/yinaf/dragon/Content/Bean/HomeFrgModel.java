package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 首页
 */
public class HomeFrgModel implements Serializable{


    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"bannerList":[{"banner_id":10,"banner_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/0810/1804081004571447.jpg","banner_jump_url":"","banner_title":"王老吉正中凉茶"}],"applicationList":[{"app_id":27,"app_state":0,"app_title":"meme","app_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/1213/1804121354167402.jpg","app_jump_url":"88889"},{"app_id":28,"app_state":0,"app_title":"好吃","app_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/1610/1804161030422457.jpg","app_jump_url":"好好吃"},{"app_id":21,"app_state":0,"app_title":"88889","app_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/0217/1804021708384104.jpg"}],"newsList":[{"new_pic":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291822334551.jpg","new_title":"是打发斯蒂芬","new_id":114}]}
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
        private List<BannerListBean> bannerList;
        private List<ApplicationListBean> applicationList;
        private List<NewsListBean> newsList;

        public List<BannerListBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<BannerListBean> bannerList) {
            this.bannerList = bannerList;
        }

        public List<ApplicationListBean> getApplicationList() {
            return applicationList;
        }

        public void setApplicationList(List<ApplicationListBean> applicationList) {
            this.applicationList = applicationList;
        }

        public List<NewsListBean> getNewsList() {
            return newsList;
        }

        public void setNewsList(List<NewsListBean> newsList) {
            this.newsList = newsList;
        }

        public static class BannerListBean {
            /**
             * banner_id : 10
             * banner_pic : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/0810/1804081004571447.jpg
             * banner_jump_url :
             * banner_title : 王老吉正中凉茶
             */

            private int banner_id;
            private String banner_pic;
            private String banner_jump_url;
            private String banner_title;

            public int getBanner_id() {
                return banner_id;
            }

            public void setBanner_id(int banner_id) {
                this.banner_id = banner_id;
            }

            public String getBanner_pic() {
                return banner_pic;
            }

            public void setBanner_pic(String banner_pic) {
                this.banner_pic = banner_pic;
            }

            public String getBanner_jump_url() {
                return banner_jump_url;
            }

            public void setBanner_jump_url(String banner_jump_url) {
                this.banner_jump_url = banner_jump_url;
            }

            public String getBanner_title() {
                return banner_title;
            }

            public void setBanner_title(String banner_title) {
                this.banner_title = banner_title;
            }
        }

        public static class ApplicationListBean {
            /**
             * app_id : 27
             * app_state : 0
             * app_title : meme
             * app_pic : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201804/1213/1804121354167402.jpg
             * app_jump_url : 88889
             */

            private int app_id;
            private int app_state;
            private String app_title;
            private String app_pic;
            private String app_jump_url;

            public int getApp_id() {
                return app_id;
            }

            public void setApp_id(int app_id) {
                this.app_id = app_id;
            }

            public int getApp_state() {
                return app_state;
            }

            public void setApp_state(int app_state) {
                this.app_state = app_state;
            }

            public String getApp_title() {
                return app_title;
            }

            public void setApp_title(String app_title) {
                this.app_title = app_title;
            }

            public String getApp_pic() {
                return app_pic;
            }

            public void setApp_pic(String app_pic) {
                this.app_pic = app_pic;
            }

            public String getApp_jump_url() {
                return app_jump_url;
            }

            public void setApp_jump_url(String app_jump_url) {
                this.app_jump_url = app_jump_url;
            }
        }

        public static class NewsListBean {
            /**
             * new_pic : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291822334551.jpg
             * new_title : 是打发斯蒂芬
             * new_id : 114
             */

            private String new_pic;
            private String new_title;
            private int new_id;

            public String getNew_pic() {
                return new_pic;
            }

            public void setNew_pic(String new_pic) {
                this.new_pic = new_pic;
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
}
