package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AppSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：关联 APP列表接口
 */

public class AppSetAPI extends DragonAPI {

    AppSetListener listener ;
    public int memberId;
    public String sessionId;

    public AppSetAPI(AppSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[AppSet-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"App",data.toString());
        // TODO Auto-generated method stub
        AppSetModel drugSetModel = JSONUtils.parseJson(data.toString(),AppSetModel.class);

        listener.appSetSuccess(drugSetModel);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.appSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/userAppList"+"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&memberId="+memberId;
    }


    public interface AppSetListener{
        public void appSetSuccess(AppSetModel drugSetModel);
        public void appSetError(long code, String msg);
    }
}