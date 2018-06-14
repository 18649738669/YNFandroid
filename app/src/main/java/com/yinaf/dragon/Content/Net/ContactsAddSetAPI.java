package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
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

public class ContactsAddSetAPI extends DragonAPI {

    ContactsAddSetListener listener ;


    public ContactsAddSetAPI(ContactsAddSetListener listener, int  memberId, String address
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

        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        ContactsSetModel model = JSONUtils.parseJson(data.toString(), ContactsSetModel.class);

        listener.contactsAddSetSuccess(model);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.contactsAddSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/addContacts";
    }


    public interface ContactsAddSetListener{
        public void contactsAddSetSuccess(ContactsSetModel drugSetModel);
        public void contactsAddSetError(long code, String msg);
    }
}