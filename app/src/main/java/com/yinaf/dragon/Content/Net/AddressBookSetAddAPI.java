package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能： 通讯录添加列表接口
 */

public class AddressBookSetAddAPI extends DragonAPI {

    AddressBookSetAddListener listener ;


    public AddressBookSetAddAPI(AddressBookSetAddListener listener, int  memberId, String address
            , String phone, String rela
            , String trueName, String province, String city, String areas) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("memberId", memberId);
        addParams("address", address);
        addParams("phone", phone);
        addParams("rela", rela);
        addParams("trueName", trueName);
        addParams("province", province);
        addParams("city", city);
        addParams("areas", areas);


        LogUtils.d("[AddressBookSet-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressBookSetAddSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressBookSetAddError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/addBooks";
    }


    public interface AddressBookSetAddListener{
        public void addressBookSetAddSuccess();
        public void addressBookSetAddError(long code, String msg);
    }
}