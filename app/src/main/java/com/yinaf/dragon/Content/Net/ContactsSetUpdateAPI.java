package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能： 修改联系人列表接口
 */

public class ContactsSetUpdateAPI extends DragonAPI {

    ContactsSetUpdateListener listener ;


    public ContactsSetUpdateAPI(ContactsSetUpdateListener listener, int  memberId,
                                int  contactsId, String address
            , String phone, String rela
            , String trueName, String province, String city, String areas) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("contactsId", contactsId);
        addParams("memberId", memberId);
        addParams("address", address);
        addParams("phone", phone);
        addParams("rela", rela);
        addParams("trueName", trueName);
        addParams("province", province);
        addParams("city", city);
        addParams("areas", areas);

        LogUtils.d("[ContactsSetUpdate-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.contactsSetUpdateSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.contactsSetUpdateError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/editContacts";
    }


    public interface ContactsSetUpdateListener{
        public void contactsSetUpdateSuccess();
        public void contactsSetUpdateError(long code, String msg);
    }
}