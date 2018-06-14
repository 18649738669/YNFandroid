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
 * 功能：获取成员历史定位统计接口
 */

public class GetLocusCountAPI extends DragonAPI {

    public GetLocusCountListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetLocusCountAPI(GetLocusCountListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetLocusCount-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/getLocusCount"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getLocusCountSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getLocusCountError(code,msg);

    }

    public interface GetLocusCountListener{
        public void getLocusCountSuccess(JSONArray content);
        public void getLocusCountError(long code, String msg);
    }
}
