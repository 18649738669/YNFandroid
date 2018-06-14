package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：通讯录列表接口
 */

public class AddressBookSetAPI extends DragonAPI {

    AddressBookSetListener listener ;
    public int memberId;
    public String sessionId;

    public AddressBookSetAPI(AddressBookSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[AddressBook-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"AddressBook",data.toString());
        // TODO Auto-generated method stub
        AddressBookSetModel model = JSONUtils.parseJson(data.toString(), AddressBookSetModel.class);

        listener.addressBookSetSuccess(model);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressBookSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/booksList"+"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&memberId="+memberId;
    }


    public interface AddressBookSetListener{
        public void addressBookSetSuccess(AddressBookSetModel drugSetModel);
        public void addressBookSetError(long code, String msg);
    }
}