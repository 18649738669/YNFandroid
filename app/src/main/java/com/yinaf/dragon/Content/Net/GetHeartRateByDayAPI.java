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
 * 功能：获取成员每天心率变化
 */

public class GetHeartRateByDayAPI extends DragonAPI {

    public GetHeartRateByDayListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetHeartRateByDayAPI(GetHeartRateByDayListener listener, String day, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.day = day;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetHeartRateByDay-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getHeartRateByDay"+"?memberId="+memberId+"&day="+day+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getHeartRateByDaySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getHeartRateByDayError(code,msg);

    }

    public interface GetHeartRateByDayListener{
        public void getHeartRateByDaySuccess(JSONObject content);
        public void getHeartRateByDayError(long code, String msg);
    }
}
