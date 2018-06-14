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
 * 功能：获取成员手表电量接口
 */

public class GetWatchBatteryAPI extends DragonAPI {

    public GetWatchBatteryListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetWatchBatteryAPI(GetWatchBatteryListener listener, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetWatchBattery-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getWatchBattery"+"?memberId="+memberId+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        listener.getWatchBatterySuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getWatchBatteryError(code,msg);

    }

    public interface GetWatchBatteryListener{
        public void getWatchBatterySuccess(JSONObject content);
        public void getWatchBatteryError(long code, String msg);
    }
}
