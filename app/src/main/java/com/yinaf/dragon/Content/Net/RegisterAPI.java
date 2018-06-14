package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-13.
 * 功能：注册接口
 */

public class RegisterAPI extends DragonAPI {
    public RegisterAPIListener listener;

    public RegisterAPI(RegisterAPIListener listener,String userName,String yzcode,String passWord){

        this.listener = listener;
        addParams("userName", userName);
        addParams("yzcode", yzcode);
        addParams("passWord", passWord);
        LogUtils.d("[Register-params]" + gson.toJson(params));
        new DragonHttp(this).request();

    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/reg";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }


    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.registerSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {
        listener.registerError(code,msg);
    }

    public interface RegisterAPIListener{
        public void registerSuccess(JSONObject content);
        public void registerError(long code,String msg);
    }
}
