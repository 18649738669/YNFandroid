package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：分页获取我的反馈数据集合接口
 */

public class FindFeedbackListAPI extends DragonAPI {

    public FindFeedbackListListener listener;
    public String memberId;
    public String sessionId;
    public String rows;
    public String page;
    public FindFeedbackListAPI(FindFeedbackListListener listener,int page){
        this.listener = listener;
        this.rows = 20 + "";
        this.page = page + "";
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[FindFeedbackList-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/feedback/findFeedbackList"+"?sessionId="+sessionId+"&rows="+rows+"&page="+page;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        listener.findFeedbackListSuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.findFeedbackListError(code,msg);

    }

    public interface FindFeedbackListListener{
        public void findFeedbackListSuccess(JSONObject content);
        public void findFeedbackListError(long code, String msg);
    }
}
