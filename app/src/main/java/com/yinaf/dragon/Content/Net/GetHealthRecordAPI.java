package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-27.
 * 功能：获取成员对应月份报告详情接口
 */

public class GetHealthRecordAPI extends DragonAPI {

    public GetHealthRecordListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetHealthRecordAPI(GetHealthRecordListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetHeartRateToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getHealthRecord"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getHealthRecordSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getHealthRecordError(code,msg);

    }

    public interface GetHealthRecordListener{
        public void getHealthRecordSuccess(JSONObject content);
        public void getHealthRecordError(long code, String msg);
    }
}
