package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Bean.MemberIntegralDetailModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

public class EnterActivityAPI extends DragonAPI {

    EnterActivityListener listener ;
    public int memberId;
    public String actId;
    public String sessionId;

    public EnterActivityAPI(EnterActivityListener listener, String actid, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.actId =actid;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {

        listener.enterActivitySuccess("报名成功，记得准时参加活动！");
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.enterActivityError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/enterActivity"+
                "?sessionId="+sessionId+"&memberId="+memberId+"&actId="+actId;
    }


    public interface EnterActivityListener{
        public void enterActivitySuccess(String drugSetModel);
        public void enterActivityError(long code, String msg);
    }
}
