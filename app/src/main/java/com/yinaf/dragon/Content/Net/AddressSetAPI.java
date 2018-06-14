package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：获取地址列表接口
 */

public class AddressSetAPI extends DragonAPI {

    AddressSetListener listener ;
    public int memberId;
    public String sessionId;

    public AddressSetAPI(AddressSetListener listener, int  memberId) {
        this.listener = listener;
        this.memberId = memberId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,memberId+"Address",data.toString());
        // TODO Auto-generated method stub
        AddressSetModel model = JSONUtils.parseJson(data.toString(), AddressSetModel.class);

        listener.addressSetSuccess(model);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addressSetError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/getMemberAddress"+"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&memberId="+memberId;
    }


    public interface AddressSetListener{
        public void addressSetSuccess(AddressSetModel drugSetModel);
        public void addressSetError(long code, String msg);
    }
}