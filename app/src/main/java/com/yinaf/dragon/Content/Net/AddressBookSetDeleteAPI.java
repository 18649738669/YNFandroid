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

public class AddressBookSetDeleteAPI extends DragonAPI {

    AddressBookSetDeleteListener listener ;


    public AddressBookSetDeleteAPI(AddressBookSetDeleteListener listener, int  bookId) {
        this.listener = listener;

        addParams("sessionId",SPHelper.getString(Builds.SP_USER,"sessionId"));
        addParams("bookId", bookId);


        LogUtils.d("[AddressBookSetDelete-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.addressBookSetDeleteSuccess();
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressBookSetDeleteError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/delBooks";
    }


    public interface AddressBookSetDeleteListener{
        public void addressBookSetDeleteSuccess();
        public void addressBookSetDeleteError(long code, String msg);
    }
}