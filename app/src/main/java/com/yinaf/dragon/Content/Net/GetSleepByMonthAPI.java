package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员每月睡眠时间变化接口
 */

public class GetSleepByMonthAPI extends DragonAPI {

    public GetSleepByMonthListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetSleepByMonthAPI(GetSleepByMonthListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetSleepToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getSleepByMonth"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getSleepByMonthSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getSleepByMonthError(code,msg);

    }

    public interface GetSleepByMonthListener{
        public void getSleepByMonthSuccess(JSONArray content);
        public void getSleepByMonthError(long code, String msg);
    }
}
