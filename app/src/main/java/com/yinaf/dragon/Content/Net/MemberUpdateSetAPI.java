package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：药物提醒列表接口
 */

public class MemberUpdateSetAPI extends DragonAPI {

    MemberUpdateSetListener listener ;


    public MemberUpdateSetAPI(MemberUpdateSetListener listener, int  memberId, String params_name, String params) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);


        addParams(params_name, params);



        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }



    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.memberUpdateSetSuccess();
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.memberUpdateSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/updateMember";
    }


    public interface MemberUpdateSetListener{

        void memberUpdateSetError(long code, String msg);

        void memberUpdateSetSuccess();
    }
}
