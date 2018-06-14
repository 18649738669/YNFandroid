package com.yinaf.dragon.Content.Net;

import android.content.Context;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-4-17.
 * 功能：获取登录用户所有成员
 */

public class GetMemberAPI extends DragonAPI{

    public GetMemberAPIListener listener;
    public String sessionId;

    public GetMemberAPI(GetMemberAPIListener listener){

        this.listener = listener;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetMember-params]" + gson.toJson(params));
        new DragonHttp(this).request();

    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/getMemberList"+"?sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) {

        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getMemberSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.getMemberError(code,msg);


    }

    public interface GetMemberAPIListener{
        public void getMemberSuccess(JSONArray content);
        public void getMemberError(long code, String msg);
    }

}
