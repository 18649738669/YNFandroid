package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：删除反馈接口
 */

public class DelFeedbackAPI extends DragonAPI {

    DelFeedbackListener listener ;

    public DelFeedbackAPI(DelFeedbackListener listener, int feedbackId) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("feedbackId", feedbackId);
        LogUtils.d("[del_Feedback-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.delFeedbackSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.delFeedbackError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/feedback/delFeedback" ;
    }


    public interface DelFeedbackListener{
        public void delFeedbackSuccess();
        public void delFeedbackError(long code, String msg);
    }
}