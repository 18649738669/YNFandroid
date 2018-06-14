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

public class DrugSetUpdateAPI extends DragonAPI {

    DrugSetUpdateListener listener ;


    public DrugSetUpdateAPI(DrugSetUpdateListener listener, int  memberId, int  medicineId
            , int watchId, String medicineTitle
            , String medicineRemarks, String startTime
            , String endTime, String remindTime) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);
        addParams("medicineId", medicineId);

        addParams("watchId", watchId);
        addParams("medicineTitle", medicineTitle);
        addParams("medicineRemarks", medicineRemarks);
        addParams("startTime", startTime);
        addParams("endTime", endTime);
        addParams("remindTime", remindTime);


        LogUtils.d("[DrugSetUpdate-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.drugSetUpdateSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.drugSetUpdateError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/updateWatchMedicine";
    }


    public interface DrugSetUpdateListener{
        public void drugSetUpdateSuccess();
        public void drugSetUpdateError(long code, String msg);
    }
}