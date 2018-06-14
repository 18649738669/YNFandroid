package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.FrequencySetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：发送频率接口
 */

public class FrequencySetAPI extends DragonAPI {

    FrequencySetListener listener ;
    public int memberId;
    public String sessionId;

    public FrequencySetAPI(FrequencySetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Frequency",data.toString());
        // TODO Auto-generated method stub
        FrequencySetModel drugSetModel = JSONUtils.parseJson(data.toString(),FrequencySetModel.class);

        listener.frequencySetSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.frequencySetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/getRate"+"?sessionId="+sessionId+"&memberId="+memberId;
    }


    public interface FrequencySetListener{
        public void frequencySetSuccess(FrequencySetModel drugSetModel);
        public void frequencySetError(long code, String msg);
    }
}