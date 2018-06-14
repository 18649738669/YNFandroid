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
 * 功能：获取成员每周睡眠时间变化接口
 */

public class GetSleepByWeekAPI extends DragonAPI {

    public GetSleepByWeekListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetSleepByWeekAPI(GetSleepByWeekListener listener, String day, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.day = day;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetSleepToWeek-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getSleepByWeek"+"?memberId="+memberId+"&day="+day+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getSleepByWeekSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getSleepByWeekError(code,msg);

    }

    public interface GetSleepByWeekListener{
        public void getSleepByWeekSuccess(JSONArray content);
        public void getSleepByWeekError(long code, String msg);
    }
}
