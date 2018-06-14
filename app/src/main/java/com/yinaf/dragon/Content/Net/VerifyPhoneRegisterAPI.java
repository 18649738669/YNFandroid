package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-4-12.
 * 功能：验证手机号是否已经注册接口
 */

public class VerifyPhoneRegisterAPI extends DragonAPI {

    public VerifyPhoneRegisterListener listener;
    public VerifyPhoneRegisterAPI(VerifyPhoneRegisterListener listener,String userName){
        this.listener = listener;
        addParams("userName", userName);
        LogUtils.d("[VerifyPhoneRegister-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/isUserExist";
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.verifySuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.verifyError(code,msg);

    }

    public interface VerifyPhoneRegisterListener{
        public void verifySuccess(JSONObject content);
        public void verifyError(long code, String msg);
    }
}
