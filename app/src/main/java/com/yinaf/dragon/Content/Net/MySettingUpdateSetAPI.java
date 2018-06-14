package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：用户基本资料更新
 */

public class MySettingUpdateSetAPI extends DragonAPI {

    MySettingUpdateSetListener listener ;


    public MySettingUpdateSetAPI(MySettingUpdateSetListener listener, String params_name, String params1) {
        super();
        this.listener = listener;
        addParams(params_name, params1);

        LogUtils.d("[my_setting-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }



    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.mySettingUpdateSetSuccess();
        Log.e("my_setting-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.mySettingUpdateSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/editUser";
    }


    public interface MySettingUpdateSetListener{

        void mySettingUpdateSetError(long code, String msg);

        void mySettingUpdateSetSuccess();
    }
}
