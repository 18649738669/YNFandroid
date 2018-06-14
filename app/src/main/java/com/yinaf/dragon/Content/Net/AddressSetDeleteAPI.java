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

public class AddressSetDeleteAPI extends DragonAPI {

    AddressSetDeleteListener listener ;


    public AddressSetDeleteAPI(AddressSetDeleteListener listener, int  memberId) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("addressId", memberId);



        LogUtils.d("[AddressSetDelete-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressSetDeleteSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressSetDeleteError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/deleteMemberAddress";
    }


    public interface AddressSetDeleteListener{
        public void addressSetDeleteSuccess();
        public void addressSetDeleteError(long code, String msg);
    }
}