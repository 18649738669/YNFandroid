package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：新增反馈接口
 * (1：APP应用 2：腕表 3：耗电 4：腕表外观 5：其它)
 */

public class AddFeedbackAPI extends DragonAPI {

    AddFeedbackListener listener ;

    public AddFeedbackAPI(AddFeedbackListener listener, int feedbackType, String feedbackContent,
                          String phone,String qq, String mail) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("feedbackType", feedbackType);
        addParams("feedbackContent", feedbackContent);
        addParams("phone", phone);
        addParams("qq", qq);
        addParams("mail", mail);
        LogUtils.d("[add_Feedback-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addFeedbackSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addFeedbackError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/feedback/addFeedback" ;
    }


    public interface AddFeedbackListener{
        public void addFeedbackSuccess();
        public void addFeedbackError(long code, String msg);
    }
}