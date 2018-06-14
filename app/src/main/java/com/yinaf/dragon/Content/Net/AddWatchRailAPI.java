package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/05/05.
 * 功能：新增安全围栏接口
 */

public class AddWatchRailAPI extends DragonAPI {

    AddWatchRailListener listener ;

    public AddWatchRailAPI(AddWatchRailListener listener, int watchId, int radius, String name,
                           String lat, String lon, String address, String lastAction) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("watchId", watchId);
        addParams("radius", radius);
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
        listener.addWatchRailSuccess(content);
        Log.e("add_WatchRail-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addWatchRailError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/addWatchRail" ;
    }


    public interface AddWatchRailListener{
        public void addWatchRailSuccess(JSONObject content);
        public void addWatchRailError(long code, String msg);
    }
}