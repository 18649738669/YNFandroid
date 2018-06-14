package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-5-7.
 * 功能：绑定腕表接口
 */

public class BindWatchAPI extends DragonAPI {

    public BindWatchAPIListener listener;

    public BindWatchAPI(BindWatchAPIListener listener, int memberId, String watchImei){

        this.listener = listener;
        addParams("memberId", memberId);
        addParams("watchImei", watchImei);
        LogUtils.d("[BindWatch-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/bindWatch";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {


        listener.bindWatchSuccess(data);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.bindWatchError(code,msg);
    }

    public interface BindWatchAPIListener{
        public void bindWatchSuccess(JSONObject content);
        public void bindWatchError(long code, String msg);
    }
}
