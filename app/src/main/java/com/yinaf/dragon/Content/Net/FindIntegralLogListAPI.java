package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：分页获取积分明细(日志)列表接口
 */

public class FindIntegralLogListAPI extends DragonAPI {

    public FindIntegralLogListListener listener;
    public String memberId;
    public String sessionId;
    public String rows;
    public String page;
    public FindIntegralLogListAPI(FindIntegralLogListListener listener, String memberId, int page){
        this.listener = listener;
        this.memberId = memberId;
        this.rows = 20 + "";
        this.page = page + "";
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[FindIntegralLogList-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/activity/findIntegralLogList"+"?memberId="+memberId+"&sessionId="+sessionId+"&rows="+rows+"&page="+page;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        listener.findIntegralLogListSuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.findIntegralLogListError(code,msg);

    }

    public interface FindIntegralLogListListener{
        public void findIntegralLogListSuccess(JSONObject content);
        public void findIntegralLogListError(long code, String msg);
    }
}
