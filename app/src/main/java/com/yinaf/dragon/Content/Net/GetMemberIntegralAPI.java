package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-5-9.
 * 功能：获取成员总积分接口
 */

public class GetMemberIntegralAPI extends DragonAPI {

    public GetMemberIntegralListener listener;
    public String memberId;
    public String sessionId;
    public GetMemberIntegralAPI(GetMemberIntegralListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetMemberIntegral-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/activity/getMemberIntegral"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getMemberIntegralSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getMemberIntegralError(code,msg);

    }

    public interface GetMemberIntegralListener{
        public void getMemberIntegralSuccess(JSONObject content);
        public void getMemberIntegralError(long code, String msg);
    }
}
