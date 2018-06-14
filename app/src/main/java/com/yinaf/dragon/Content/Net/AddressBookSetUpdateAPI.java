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

public class AddressBookSetUpdateAPI extends DragonAPI {

    AddressBookSetUpdateListener listener ;


    public AddressBookSetUpdateAPI(AddressBookSetUpdateListener listener, int  memberId,
             int  bookId, String address
            , String phone, String rela
            , String trueName, String province, String city, String areas) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("bookId", bookId);
        addParams("memberId", memberId);
        addParams("address", address);
        addParams("phone", phone);
        addParams("rela", rela);
        addParams("trueName", trueName);
        addParams("province", province);
        addParams("city", city);
        addParams("areas", areas);

        LogUtils.d("[AddressBookSetUpdate-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressBookSetUpdateSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressBookSetUpdateError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/editBooks";
    }


    public interface AddressBookSetUpdateListener{
        public void addressBookSetUpdateSuccess();
        public void addressBookSetUpdateError(long code, String msg);
    }
}