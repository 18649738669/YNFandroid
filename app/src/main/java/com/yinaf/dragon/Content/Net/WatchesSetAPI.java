package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.FootstepSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.WatchesSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：手表设置接口
 */

public class WatchesSetAPI extends DragonAPI {

    WatchesSetListener listener ;
    public int memberId;
    public String sessionId;

    public WatchesSetAPI(WatchesSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Watches",data.toString());
        // TODO Auto-generated method stub
        WatchesSetModel drugSetModel = JSONUtils.parseJson(data.toString(),WatchesSetModel.class);

        listener.footstepSetSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.footstepSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/getWatchDetail"+"?sessionId="+sessionId+"&memberId="+memberId;
    }


    public interface WatchesSetListener{
        public void footstepSetSuccess(WatchesSetModel drugSetModel);
        public void footstepSetError(long code, String msg);
    }
}