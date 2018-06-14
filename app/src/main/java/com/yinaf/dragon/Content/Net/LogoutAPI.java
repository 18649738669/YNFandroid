package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-4-12.
 * 功能：退出登录接口
 */

public class LogoutAPI extends DragonAPI {

    public LogoutAPIListener listener;

    public LogoutAPI(LogoutAPIListener listener){

        this.listener = listener;
        LogUtils.d("[Logout-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/logout";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        listener.logoutSuccess(data);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.logoutError(code,msg);
    }

    public interface LogoutAPIListener{
        public void logoutSuccess(JSONObject content);
        public void logoutError(long code, String msg);
    }
}
