package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能： 联系人删除接口
 */

public class ContactsSetDeleteAPI extends DragonAPI {

    ContactsSetDeleteListener listener ;


    public ContactsSetDeleteAPI(ContactsSetDeleteListener listener, int  contactId) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("contactId", contactId);


        LogUtils.d("[ ContactsSetDelete-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.contactsSetDeleteSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.contactsSetDeleteError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/delContacts";
    }


    public interface ContactsSetDeleteListener{
        public void contactsSetDeleteSuccess();
        public void contactsSetDeleteError(long code, String msg);
    }
}