package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesAct;
import com.yinaf.dragon.Content.Bean.LeisureActivityModel;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

public class LeisureActivityAPI extends DragonAPI {

    LeisureActivityListener listener ;
    public int memberId;
    public String sessionId;

    public LeisureActivityAPI(LeisureActivityListener listener) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,"LeisureActivity",data.toString());
        // TODO Auto-generated method stub
        LeisureActivityModel model = JSONUtils.parseJson(data.toString(),LeisureActivityModel.class);

        listener.leisureActivitySuccess(model);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.leisureActivityError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/findMobileActivityList"
                +"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&page="+1+"&rows="+100;
    }


    public interface LeisureActivityListener{
        public void leisureActivitySuccess(LeisureActivityModel drugSetModel);
        public void leisureActivityError(long code, String msg);
    }
}
