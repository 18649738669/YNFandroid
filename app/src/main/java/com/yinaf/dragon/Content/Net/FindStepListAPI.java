package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：分页获取计步数据集合接口
 */

public class FindStepListAPI extends DragonAPI {

    public FindStepListListener listener;
    public String memberId;
    public String sessionId;
    public String rows;
    public String page;
    public FindStepListAPI(FindStepListListener listener, String memberId,int page){
        this.listener = listener;
        this.memberId = memberId;
        this.rows = 20 + "";
        this.page = page + "";
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[FindStepList-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/findStepList"+"?memberId="+memberId+"&sessionId="+sessionId+"&rows="+rows+"&page="+page;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        listener.findStepListSuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.findStepListError(code,msg);

    }

    public interface FindStepListListener{
        public void findStepListSuccess(JSONObject content);
        public void findStepListError(long code, String msg);
    }
}
