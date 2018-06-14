package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;

public class MemberIntegralDetailModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : {"user_name":"18966333555","integral_num":20,"real_name":"我修改了名称","act_integral":5}
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
         * user_name : 18966333555
         * integral_num : 20
         * real_name : 我修改了名称
         * act_integral : 5
         */

        private String user_name;
        private int integral_num;
        private String real_name;
        private int act_integral;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getIntegral_num() {
            return integral_num;
        }

        public void setIntegral_num(int integral_num) {
            this.integral_num = integral_num;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public int getAct_integral() {
            return act_integral;
        }

        public void setAct_integral(int act_integral) {
            this.act_integral = act_integral;
        }
    }
}
