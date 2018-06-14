package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-5-7.
 * 功能：验证成员接口
 */

public class ValidateMemberAPI extends DragonAPI {

    public ValidateMemberAPIListener listener;

    public ValidateMemberAPI(ValidateMemberAPIListener listener, String memberNum){

        this.listener = listener;
        addParams("memberNum", memberNum);
        LogUtils.d("[ValidateMember-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/validateMember";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.validateMemberSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.validateMemberError(code,msg);
    }

    public interface ValidateMemberAPIListener{
        public void validateMemberSuccess(JSONObject content);
        public void validateMemberError(long code, String msg);
    }
}
