package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSetModel;
import com.yinaf.dragon.Content.Bean.MySettingModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * 功能：用户基本资料接口
 */

public class MySettingAPI extends DragonAPI {

    MySettingListener listener ;
    public int memberId;
    public String sessionId;

    public MySettingAPI(MySettingListener listener) {
        this.listener = listener;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
       SPHelper.save(Builds.SP_USER,"Setting",data.toString());
        // TODO Auto-generated method stub
        MySettingModel drugSetModel = JSONUtils.parseJson(data.toString(),MySettingModel.class);

        listener.memberSetSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.memberSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/getUser"+"?sessionId="+sessionId;
    }


    public interface MySettingListener{
        public void memberSetSuccess(MySettingModel drugSetModel);
        public void memberSetError(long code, String msg);
    }
}