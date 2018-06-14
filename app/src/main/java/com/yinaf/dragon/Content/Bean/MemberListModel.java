package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 成员列表
 */
public class MemberListModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * obj : [{"isLead":0,"image":"http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/img/1802071741075397_tmp.jpg","realName":"昵称吗","memberId":1,"memberNum":"123456789","rela":"父子"},{"isLead":0,"image":"http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/img/1801121754155399_tmp.jpg","realName":"笑颜","memberId":2,"memberNum":"23564789454","rela":"父子"}]
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
         * isLead : 0
         * image : http://ynf-resteraut.oss-cn-shanghai.aliyuncs.com/img/1802071741075397_tmp.jpg
         * realName : 昵称吗
         * memberId : 1
         * memberNum : 123456789
         * rela : 父子
         */

        private int isLead;
        private String image;
        private String realName;
        private int memberId;
        private String memberNum;
        private String rela;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        private boolean isSelect;

        public int getIsLead() {
            return isLead;
        }

        public void setIsLead(int isLead) {
            this.isLead = isLead;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getRela() {
            return rela;
        }

        public void setRela(String rela) {
            this.rela = rela;
        }
    }
}
