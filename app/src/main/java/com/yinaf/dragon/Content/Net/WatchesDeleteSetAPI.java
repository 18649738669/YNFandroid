package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：  手表删除接口
 */

public class WatchesDeleteSetAPI extends DragonAPI {

    WatchesDeleteSetListener listener ;


    public WatchesDeleteSetAPI(WatchesDeleteSetListener listener, int watchId) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("watchId", watchId);


        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.watchesDeleteSetSuccess();
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.watchesDeleteSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/unBindWatch";
    }


    public interface WatchesDeleteSetListener{
        public void watchesDeleteSetSuccess();
        public void watchesDeleteSetError(long code, String msg);
    }
}