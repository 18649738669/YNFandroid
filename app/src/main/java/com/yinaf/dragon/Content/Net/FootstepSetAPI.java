package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.FootstepSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.PoliceSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：发送频率接口
 */

public class FootstepSetAPI extends DragonAPI {

    FootstepSetListener listener ;
    public int memberId;
    public String sessionId;

    public FootstepSetAPI(FootstepSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Footstep",data.toString());
        // TODO Auto-generated method stub
        FootstepSetModel drugSetModel = JSONUtils.parseJson(data.toString(),FootstepSetModel.class);

        listener.footstepSetSuccess(drugSetModel);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.footstepSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/getStep"+"?sessionId="+sessionId+"&memberId="+memberId;
    }


    public interface FootstepSetListener{
        public void footstepSetSuccess(FootstepSetModel drugSetModel);
        public void footstepSetError(long code, String msg);
    }
}