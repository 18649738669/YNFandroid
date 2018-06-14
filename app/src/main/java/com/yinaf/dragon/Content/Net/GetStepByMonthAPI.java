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
 * 功能：获取成员每月步数变化接口
 */

public class GetStepByMonthAPI extends DragonAPI {

    public GetStepByMonthListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetStepByMonthAPI(GetStepByMonthListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetStepToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getStepByMonth"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getStepByMonthSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getStepByMonthError(code,msg);

    }

    public interface GetStepByMonthListener{
        public void getStepByMonthSuccess(JSONArray content);
        public void getStepByMonthError(long code, String msg);
    }
}
