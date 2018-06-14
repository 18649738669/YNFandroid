package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能： 电话,预警提醒,步长设置,发送频率 列表接口
 */

public class WatchSetAPI extends DragonAPI {

    WatchSetListener listener ;


    public WatchSetAPI(WatchSetListener listener, int  memberId
            , int watchId, String params_name, String params) {
        this.listener = listener;
        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);
        addParams("watchId", watchId);
        addParams(params_name, params);
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }
    public WatchSetAPI(WatchSetListener listener, int  memberId
            , int watchId, String params_name, String params, String params_name2, String params2) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);
        addParams("watchId", watchId);
        addParams(params_name, params);
        addParams(params_name2, params2);
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.watchSetSuccess();
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.watchSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/setWatch";
    }


    public interface WatchSetListener{
        public void watchSetSuccess();
        public void watchSetError(long code, String msg);
    }
}