package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员最新心率及异常心率数
 */

public class GetCurrentHeartRateAPI extends DragonAPI {

    public GetCurrentHeartRateListener listener;
    public String memberId;
    public String sessionId;
    public GetCurrentHeartRateAPI(GetCurrentHeartRateListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetCurrentHeartRate-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getCurrentHeartRate"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getCurrentHeartRateSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getCurrentHeartRateError(code,msg);

    }

    public interface GetCurrentHeartRateListener{
        public void getCurrentHeartRateSuccess(JSONObject content);
        public void getCurrentHeartRateError(long code, String msg);
    }
}
