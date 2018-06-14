package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-5-8.
 * 功能：获取APP版本更新接口
 */

public class GetUpdateAppAPI extends DragonAPI {

    public GetUpdateAppListener listener;
    public String serverVersion;
    public GetUpdateAppAPI(GetUpdateAppListener listener, String serverVersion){
        this.listener = listener;
        this.serverVersion = serverVersion;
        LogUtils.d("[GetUpdateApp-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/setup/getUpdateApp"+"?appType="+1+"&serverVersion="+serverVersion;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

         listener.getUpdateAppSuccess(data);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.getUpdateAppError(code,msg);

    }

    public interface GetUpdateAppListener{
        public void getUpdateAppSuccess(JSONObject content);
        public void getUpdateAppError(long code, String msg);
    }
}
