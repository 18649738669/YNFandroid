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
 * 功能：获取成员每月血压变化接口
 */

public class GetBloodSugarByMonthAPI extends DragonAPI {

    public GetBloodSugarByMonthListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetBloodSugarByMonthAPI(GetBloodSugarByMonthListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetBloodSugarToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getBloodSugarByMonth"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getBloodSugarByMonthSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getBloodSugarByMonthError(code,msg);

    }

    public interface GetBloodSugarByMonthListener{
        public void getBloodSugarByMonthSuccess(JSONArray content);
        public void getBloodSugarByMonthError(long code, String msg);
    }
}
