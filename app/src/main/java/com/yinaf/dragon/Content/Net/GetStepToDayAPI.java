package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员当天总步数接口
 */

public class GetStepToDayAPI extends DragonAPI {

    public GetStepToDayListener listener;
    public String memberId;
    public String sessionId;
    public GetStepToDayAPI(GetStepToDayListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetStepToDay-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getStepToDay"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getStepToDaySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getStepToDayError(code,msg);

    }

    public interface GetStepToDayListener{
        public void getStepToDaySuccess(JSONObject content);
        public void getStepToDayError(long code, String msg);
    }
}
