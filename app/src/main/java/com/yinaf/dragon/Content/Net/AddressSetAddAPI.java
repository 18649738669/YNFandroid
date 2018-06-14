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

public class AddressSetAddAPI extends DragonAPI {

    AddressSetAddListener listener ;


    public AddressSetAddAPI(AddressSetAddListener listener, int  memberId
            ,String receiver,String address,String phone,String region,String defcode,String zipcode) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);
        addParams("receiver", receiver);
        addParams("address", address);
        addParams("phone", phone);
        addParams("region", region);
        addParams("defcode", defcode);
        addParams("zipcode", zipcode);



        LogUtils.d("[AddressSetAdd-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressSetAddSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressSetAddError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/addMemberAddress";
    }


    public interface AddressSetAddListener{
        public void addressSetAddSuccess();
        public void addressSetAddError(long code, String msg);
    }
}