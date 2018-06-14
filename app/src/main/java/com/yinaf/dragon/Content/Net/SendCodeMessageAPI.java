package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-13.
 * 功能：发送验证码接口
 */

public class SendCodeMessageAPI extends DragonAPI {

    public SendCodeMessageListener listener;
    public String userNo;
    public SendCodeMessageAPI(SendCodeMessageListener listener, String userNo){
        this.listener = listener;
        this.userNo = userNo;
        LogUtils.d("[SendCodeMessage-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/sendMsg"+"?userNo="+userNo;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.codeMessageSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.codeMessageError(code,msg);

    }

    public interface SendCodeMessageListener{
        public void codeMessageSuccess(JSONObject content);
        public void codeMessageError(long code, String msg);
    }
}
