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
 * 功能：获取健康报告月份统计接口
 */

public class GetHealthRecordByMonthAPI extends DragonAPI {

    public GetHealthRecordByMonthListener listener;
    public String memberId;
    public String sessionId;
    public GetHealthRecordByMonthAPI(GetHealthRecordByMonthListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetHealthRecordToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getHealthRecordByMonth"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getHealthRecordByMonthSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getHealthRecordByMonthError(code,msg);

    }

    public interface GetHealthRecordByMonthListener{
        public void getHealthRecordByMonthSuccess(JSONArray content);
        public void getHealthRecordByMonthError(long code, String msg);
    }
}
