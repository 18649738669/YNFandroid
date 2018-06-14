package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：分页获取心率异常数据集合接口
 */

public class FindAlertHeartRateListAPI extends DragonAPI {

    public FindAlertHeartRateListListener listener;
    public String memberId;
    public String sessionId;
    public String rows;
    public String page;
    public FindAlertHeartRateListAPI(FindAlertHeartRateListListener listener, String memberId, int page){
        this.listener = listener;
        this.memberId = memberId;
        this.rows = 20 + "";
        this.page = page + "";
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[FindAlertHeartRateList-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/findAlertHeartRateList"+"?memberId="+memberId+"&sessionId="+sessionId+"&rows="+rows+"&page="+page;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        listener.findAlertHeartRateListSuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.findAlertHeartRateListError(code,msg);

    }

    public interface FindAlertHeartRateListListener{
        public void findAlertHeartRateListSuccess(JSONObject content);
        public void findAlertHeartRateListError(long code, String msg);
    }
}
