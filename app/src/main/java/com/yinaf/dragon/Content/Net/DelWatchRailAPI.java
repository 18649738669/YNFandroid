package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：删除安全围栏接口
 */

public class DelWatchRailAPI extends DragonAPI {

    DelWatchRailListener listener ;

    public DelWatchRailAPI(DelWatchRailListener listener, int railId) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("railId", railId);
        LogUtils.d("[del_WatchRail-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.delWatchRailSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.delWatchRailError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/delWatchRail" ;
    }


    public interface DelWatchRailListener{
        public void delWatchRailSuccess();
        public void delWatchRailError(long code, String msg);
    }
}