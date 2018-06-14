package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：药物提醒列表接口
 */

public class DrugSetAPI extends DragonAPI {

    DrugSetListener listener ;
    public int memberId;
    public String sessionId;

    public DrugSetAPI(DrugSetListener listener,int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[DrugSet-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Drug",data.toString());

        // TODO Auto-generated method stub
        DrugSetModel drugSetModel = JSONUtils.parseJson(data.toString(), DrugSetModel.class);

        listener.drugSetSuccess(drugSetModel);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.drugSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/getWatchMedicine"+"?sessionId="+sessionId+"&memberId="+memberId;
    }


    public interface DrugSetListener{
        public void drugSetSuccess(DrugSetModel drugSetModel);
        public void drugSetError(long code, String msg);
    }
}