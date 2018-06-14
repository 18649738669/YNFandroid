package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：药物提醒列表接口
 */

public class MyAddressSetUpdateAPI extends DragonAPI {

    AddressSetUpdateListener listener ;


    public MyAddressSetUpdateAPI(AddressSetUpdateListener listener, int  memberId
            , String receiver, String address, String phone, String region
            , String defcode, String zipcode, String addressId) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("auId", memberId);
        addParams("receiver", receiver);
        addParams("address", address);
        addParams("phone", phone);
        addParams("region", region);
        addParams("defcode", defcode);
        addParams("zipcode", zipcode);



        LogUtils.d("[AddressSetUpdate-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressSetUpdateSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressSetUpdateError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/updateUserAddress";
    }


    public interface AddressSetUpdateListener{
        public void addressSetUpdateSuccess();
        public void addressSetUpdateError(long code, String msg);
    }
}