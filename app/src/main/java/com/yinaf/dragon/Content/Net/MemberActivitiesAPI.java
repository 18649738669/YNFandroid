package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.MemberActivitiesDetailsAct;
import com.yinaf.dragon.Content.Bean.LeisureActivitiesDetailsModel;
import com.yinaf.dragon.Content.Bean.MemberActivitiesDetailsModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

public class MemberActivitiesAPI extends DragonAPI {

    MemberActivitiesListener listener ;
    public String memberId;
    public String sessionId;

    public MemberActivitiesAPI(MemberActivitiesListener listener, String  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"MemberActivities",data.toString());
        // TODO Auto-generated method stub
        MemberActivitiesDetailsModel drugSetModel =
                JSONUtils.parseJson(data.toString(),MemberActivitiesDetailsModel.class);

        listener.memberActivitiesSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.memberActivitiesError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/getMemberActivity"+"?sessionId="+sessionId+"&maId="+memberId;
    }


    public interface MemberActivitiesListener{
        public void memberActivitiesSuccess(MemberActivitiesDetailsModel drugSetModel);
        public void memberActivitiesError(long code, String msg);
    }
}
