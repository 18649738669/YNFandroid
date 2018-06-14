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
 * 功能：获取成员每月步数变化接口
 */

public class GetHeartRateByMonthAPI extends DragonAPI {

    public GetHeartRateByMonthListener listener;
    public String memberId;
    public String sessionId;
    public String month;
    public GetHeartRateByMonthAPI(GetHeartRateByMonthListener listener, String month, String memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.month = month;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetHeartRateToMonth-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/healthy/getHeartRateByMonth"+"?memberId="+memberId+"&month="+month+"&sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONObject content = JSONUtils.getJSONObject(data,"obj");
        listener.getHeartRateByMonthSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getHeartRateByMonthError(code,msg);

    }

    public interface GetHeartRateByMonthListener{
        public void getHeartRateByMonthSuccess(JSONObject content);
        public void getHeartRateByMonthError(long code, String msg);
    }
}
