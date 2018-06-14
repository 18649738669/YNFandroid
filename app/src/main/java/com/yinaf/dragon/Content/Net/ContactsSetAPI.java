package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.ContactsSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：联系人列表接口
 */

public class ContactsSetAPI extends DragonAPI {

    ContactsSetListener listener ;
    public int memberId;
    public String sessionId;

    public ContactsSetAPI(ContactsSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[ContactsSet-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Contacts",data.toString());
        // TODO Auto-generated method stub
        ContactsSetModel model = JSONUtils.parseJson(data.toString(), ContactsSetModel.class);

        listener.contactsSetSuccess(model);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.contactsSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/contactsList"+"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&memberId="+memberId;
    }


    public interface ContactsSetListener{
        public void contactsSetSuccess(ContactsSetModel drugSetModel);
        public void contactsSetError(long code, String msg);
    }
}