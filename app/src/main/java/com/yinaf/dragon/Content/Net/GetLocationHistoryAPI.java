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
 * 功能：获取成员当天定位接口
 */

public class GetLocationHistoryAPI extends DragonAPI {

    public GetLocationHistoryListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetLocationHistoryAPI(GetLocationHistoryListener listener, String day, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.day = day;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetLocationHistory-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/getLocationHistory"+"?memberId="+memberId+"&day="+day+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getLocationHistorySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getLocationHistoryError(code,msg);

    }

    public interface GetLocationHistoryListener{
        public void getLocationHistorySuccess(JSONArray content);
        public void getLocationHistoryError(long code, String msg);
    }
}
