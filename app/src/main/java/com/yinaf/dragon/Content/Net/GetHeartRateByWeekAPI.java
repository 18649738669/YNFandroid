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
 * 功能：获取成员每周步数变化接口
 */

public class GetHeartRateByWeekAPI extends DragonAPI {

    public GetHeartRateByWeekListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetHeartRateByWeekAPI(GetHeartRateByWeekListener listener, String day, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.day = day;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetHeartRateToWeek-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getHeartRateByWeek"+"?memberId="+memberId+"&day="+day+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getHeartRateByWeekSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getHeartRateByWeekError(code,msg);

    }

    public interface GetHeartRateByWeekListener{
        public void getHeartRateByWeekSuccess(JSONObject content);
        public void getHeartRateByWeekError(long code, String msg);
    }
}