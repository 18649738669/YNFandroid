package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-5-7.
 * 功能：绑定成员接口
 */

public class BindMemberAPI extends DragonAPI {

    public BindMemberAPIListener listener;

    public BindMemberAPI(BindMemberAPIListener listener,String userName, int memberId, String yzcode){

        this.listener = listener;
        addParams("userName", userName);
        addParams("memberId", memberId);
        addParams("yzcode", yzcode);
        LogUtils.d("[BindMember-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/bindMember";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        listener.bindMemberSuccess(data);


    }

    @Override
    public void requestError(long code, String msg) {

        listener.bindMemberError(code,msg);
    }

    public interface BindMemberAPIListener{
        public void bindMemberSuccess(JSONObject content);
        public void bindMemberError(long code, String msg);
    }
}
