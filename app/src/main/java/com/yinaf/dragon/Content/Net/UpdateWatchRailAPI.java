package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/05/05.
 * 功能：修改安全围栏接口
 */

public class UpdateWatchRailAPI extends DragonAPI {

    UpdateWatchRailListener listener ;

    public UpdateWatchRailAPI(UpdateWatchRailListener listener,int railId, int watchId, int radius, String name,
                              String lat, String lon, String address, String lastAction) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("watchId", watchId);
        addParams("radius", radius);
        addParams("railId", railId);
        addParams("name", name);
        addParams("lat", lat);
        addParams("lon", lon);
        addParams("address", address);
        addParams("lastAction", lastAction);
        LogUtils.d("[add_WatchRail-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        JSONObject content = new JSONObject();
        listener.updateWatchRailSuccess(content);
        Log.e("add_WatchRail-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.updateWatchRailError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/updateWatchRail" ;
    }


    public interface UpdateWatchRailListener{
        public void updateWatchRailSuccess(JSONObject content);
        public void updateWatchRailError(long code, String msg);
    }
}