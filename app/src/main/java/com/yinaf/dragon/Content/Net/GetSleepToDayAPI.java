package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员当天总睡眠时间接口
 */

public class GetSleepToDayAPI extends DragonAPI {

    public GetSleepToDayListener listener;
    public String memberId;
    public String sessionId;
    public GetSleepToDayAPI(GetSleepToDayListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetSleepToDay-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getSleepToDay"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getSleepToDaySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getSleepToDayError(code,msg);

    }

    public interface GetSleepToDayListener{
        public void getSleepToDaySuccess(JSONObject content);
        public void getSleepToDayError(long code, String msg);
    }
}
