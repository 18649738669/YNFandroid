package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取体检报告详情接口
 */

public class GetDeviceReportAPI extends DragonAPI {

    public GetDeviceReportListener listener;
    public int drId;
    public String sessionId;
    public GetDeviceReportAPI(GetDeviceReportListener listener,  int drId){
        this.listener = listener;
        this.drId = drId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetDeviceReport-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/device/getDeviceReport"+"?drId="+drId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");

        listener.getDeviceReportSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getDeviceReportError(code,msg);

    }

    public interface GetDeviceReportListener{
        public void getDeviceReportSuccess(JSONObject content);
        public void getDeviceReportError(long code, String msg);
    }
}
