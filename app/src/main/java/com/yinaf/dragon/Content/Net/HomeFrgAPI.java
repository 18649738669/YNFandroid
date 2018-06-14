package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.Content.Bean.MySettingModel;
import com.yinaf.dragon.Content.Fragment.HomeFrg;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * 首页广告、资讯及我的标签接口
 */
public class HomeFrgAPI extends DragonAPI {

    HomeFrgListener listener ;
    public String sessionId;

    public HomeFrgAPI(HomeFrgListener listener) {
        this.listener = listener;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,"HomeFrg",data.toString());
        // TODO Auto-generated method stub
        HomeFrgModel drugSetModel = JSONUtils.parseJson(data.toString(),HomeFrgModel.class);

        listener.homeFrgSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.homeFrgError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/index/getIndex"+"?sessionId="+sessionId;
    }


    public interface HomeFrgListener{
        public void homeFrgSuccess(HomeFrgModel drugSetModel);
        public void homeFrgError(long code, String msg);
    }
}
