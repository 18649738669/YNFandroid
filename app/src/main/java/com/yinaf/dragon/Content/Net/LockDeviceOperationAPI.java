package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018/05/21.
 * 操作门锁
 * 操作码
 *“06” 开锁，”08”解绑门锁，”38”关锁
 *
 */

public class LockDeviceOperationAPI extends DragonAPI{

    LockDeviceOperationAPIListener listener;

    public LockDeviceOperationAPI(LockDeviceOperationAPIListener listener, String deviceId,
                                  String subId, String operation) {
        this.listener = listener;
        addParams("deviceId", deviceId);
        addParams("subId", subId);
        addParams("operation", operation);
        LogUtils.d("[LockDeviceOperation-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/device/lockDeviceOperation";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        listener.lockDeviceOperationSuccess(data);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.lockDeviceOperationError(code,msg);
    }
    public interface LockDeviceOperationAPIListener{
        public void lockDeviceOperationSuccess(JSONObject content);
        public void lockDeviceOperationError(long code, String msg);
    }
}
