package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：反馈评价接口
 */

public class FeedbackAssessAPI extends DragonAPI {

    FeedbackAssessListener listener ;

    public FeedbackAssessAPI(FeedbackAssessListener listener, int feedbackId, int assess) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("feedbackId", feedbackId);
        addParams("assess", assess);
        LogUtils.d("[FeedbackAssess-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        JSONArray content = new JSONArray();
        listener.feedbackAssessSuccess(content);
        Log.e("FeedbackAssess-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.feedbackAssessError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/feedback/feedbackAssess" ;
    }


    public interface FeedbackAssessListener{
        public void feedbackAssessSuccess(JSONArray content);
        public void feedbackAssessError(long code, String msg);
    }
}