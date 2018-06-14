package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-4-17.
 * 功能：获取反馈详情
 */

public class GetFeedbackAPI extends DragonAPI{

    public GetFeedbackAPIListener listener;
    public String sessionId;
    public int feedbackId;

    public GetFeedbackAPI(GetFeedbackAPIListener listener, int feedbackId){

        this.listener = listener;
        this.feedbackId = feedbackId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetFeedback-params]" + gson.toJson(params));
        new DragonHttp(this).request();

    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/feedback/getFeedback"+"?sessionId="+sessionId+"&feedbackId="+feedbackId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) {

        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getFeedbackSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.getFeedbackError(code,msg);


    }

    public interface GetFeedbackAPIListener{
        public void getFeedbackSuccess(JSONObject content);
        public void getFeedbackError(long code, String msg);
    }

}
