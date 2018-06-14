package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.ApplyActivityAct;
import com.yinaf.dragon.Content.Bean.LeisureActivitiesDetailsModel;
import com.yinaf.dragon.Content.Bean.MemberIntegralDetailModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

public class MemberIntegralDetailAPI extends DragonAPI {

    MemberIntegralDetailListener listener ;
    public int memberId;
    public String actId;
    public String sessionId;

    public MemberIntegralDetailAPI(MemberIntegralDetailListener listener,String actid, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.actId =actid;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Leisure",data.toString());
        // TODO Auto-generated method stub
        MemberIntegralDetailModel drugSetModel =
                JSONUtils.parseJson(data.toString(),MemberIntegralDetailModel.class);

        listener.memberIntegralDetailSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.memberIntegralDetailError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/activity/getMemberIntegralDetail"+
                "?sessionId="+sessionId+"&memberId="+memberId+"&actId="+actId;
    }


    public interface MemberIntegralDetailListener{
        public void memberIntegralDetailSuccess(MemberIntegralDetailModel drugSetModel);
        public void memberIntegralDetailError(long code, String msg);
    }
}
