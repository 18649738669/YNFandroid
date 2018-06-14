package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员当天血糖接口
 */

public class GetBloodSugarToDayAPI extends DragonAPI {

    public GetBloodSugarToDayListener listener;
    public String memberId;
    public String sessionId;
    public GetBloodSugarToDayAPI(GetBloodSugarToDayListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetBloodSugarToDay-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getBloodSugarToDay"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getBloodSugarToDaySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getBloodSugarToDayError(code,msg);

    }

    public interface GetBloodSugarToDayListener{
        public void getBloodSugarToDaySuccess(JSONObject content);
        public void getBloodSugarToDayError(long code, String msg);
    }
}
