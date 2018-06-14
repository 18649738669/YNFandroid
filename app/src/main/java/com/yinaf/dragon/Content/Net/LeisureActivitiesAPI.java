package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesDetailsAct;
import com.yinaf.dragon.Content.Activity.family_set.model.FrequencySetModel;
import com.yinaf.dragon.Content.Bean.LeisureActivitiesDetailsModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

/**
 * 休闲活动详情
 */
public class LeisureActivitiesAPI extends DragonAPI {

    LeisureActivitiesListener listener ;
    public String memberId;
    public String sessionId;

    public LeisureActivitiesAPI(LeisureActivitiesListener listener, String  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Leisure",data.toString());
        // TODO Auto-generated method stub
        LeisureActivitiesDetailsModel drugSetModel = JSONUtils.parseJson(data.toString(),LeisureActivitiesDetailsModel.class);

        listener.leisureActivitiesSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.leisureActivitiesError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/getMobileActivity"+"?sessionId="+sessionId+"&actId="+memberId;
    }


    public interface LeisureActivitiesListener{
        public void leisureActivitiesSuccess(LeisureActivitiesDetailsModel drugSetModel);
        public void leisureActivitiesError(long code, String msg);
    }
}
