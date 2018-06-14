package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-4-12.
 * 功能：登录接口
 */

public class LoginAPI extends DragonAPI {

    public LoginAPIListener listener;

    public LoginAPI(LoginAPIListener listener, String userName, String sessionId, String passWord){

        this.listener = listener;
        addParams("userName", userName);
        addParams("passWord", passWord);
        addParams("sessionId", sessionId);
        LogUtils.d("[Login-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/login";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.loginSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.loginError(code,msg);
    }

    public interface LoginAPIListener{
        public void loginSuccess(JSONObject content);
        public void loginError(long code,String msg);
    }
}
