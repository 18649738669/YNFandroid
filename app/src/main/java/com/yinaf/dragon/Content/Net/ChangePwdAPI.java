package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-13.
 * 功能：修改密码接口
 */

public class ChangePwdAPI extends DragonAPI {
    public ChangePwdAPIListener listener;

    public ChangePwdAPI(ChangePwdAPIListener listener, String userName, String yzcode, String passWord){

        this.listener = listener;
        addParams("userName", userName);
        addParams("yzcode", yzcode);
        addParams("passWord", passWord);
        LogUtils.d("[Register-params]" + gson.toJson(params));
        new DragonHttp(this).request();

    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/resetUserPwd";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }


    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.changeSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {
        listener.changeError(code,msg);
    }

    public interface ChangePwdAPIListener{
        public void changeSuccess(JSONObject content);
        public void changeError(long code, String msg);
    }
}
