package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018/05/21.
 * 添加智能设备
 */

public class LockAddDeviceAPI extends DragonAPI{

    LockAddDeviceAPIListener listener;

    public LockAddDeviceAPI(LockAddDeviceAPIListener listener,String deviceId, String deviceName) {
        this.listener = listener;
        addParams("deviceId", deviceId);
        addParams("deviceName", deviceName);
        LogUtils.d("[LockAddDevice-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/device/lockAddDevice";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        listener.lockAddDeviceSuccess(data);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.lockAddDeviceError(code,msg);
    }
    public interface LockAddDeviceAPIListener{
        public void lockAddDeviceSuccess(JSONObject content);
        public void lockAddDeviceError(long code,String msg);
    }
}
