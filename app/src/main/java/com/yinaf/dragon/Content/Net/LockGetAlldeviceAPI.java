package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018/05/21.
 * 我的设备列表
 */

public class LockGetAlldeviceAPI extends DragonAPI{

    LockGetAlldeviceAPIListener listener;

    public LockGetAlldeviceAPI(LockGetAlldeviceAPIListener listener) {
        this.listener = listener;
        LogUtils.d("[LockGetAlldevice-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/device/lockGetAlldevice";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.lockGetAlldeviceSuccess(content);
        Log.d("LockGetAlldevice",content.toString());

    }

    @Override
    public void requestError(long code, String msg) {

        listener.lockGetAlldeviceError(code,msg);
    }
    public interface LockGetAlldeviceAPIListener{
        public void lockGetAlldeviceSuccess(JSONArray content);
        public void lockGetAlldeviceError(long code, String msg);
    }
}
