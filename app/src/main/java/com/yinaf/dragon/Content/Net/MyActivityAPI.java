package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：我的活动列表接口
 */

public class MyActivityAPI extends DragonAPI {

    MyActivityListener listener ;
    public int memberId;
    public String sessionId;

    public MyActivityAPI(MyActivityListener listener) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,"Activity",data.toString());
        // TODO Auto-generated method stub
        MyActivityModel model = JSONUtils.parseJson(data.toString(), MyActivityModel.class);

        listener.myActivitySuccess(model);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.myActivityError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/findMemberActivityList"
                +"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&page="+1+"&rows="+100;
    }


    public interface MyActivityListener{
        public void myActivitySuccess(MyActivityModel drugSetModel);
        public void myActivityError(long code, String msg);
    }
}