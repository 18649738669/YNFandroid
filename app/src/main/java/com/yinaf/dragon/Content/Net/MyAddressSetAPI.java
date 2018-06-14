package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Content.Bean.MyAddressSetModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：获取地址列表接口
 */

public class MyAddressSetAPI extends DragonAPI {

    AddressSetListener listener ;
    public String useid;
    public String sessionId;

    public MyAddressSetAPI(AddressSetListener listener, String  useid) {
        this.listener = listener;
        this.useid = useid;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        SPHelper.save(Builds.SP_USER,useid+"MyAddress",data.toString());
        // TODO Auto-generated method stub
        MyAddressSetModel model = JSONUtils.parseJson(data.toString(), MyAddressSetModel.class);

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
        return Builds.HOST + "/mobile/user/getUserAddress"+"?sessionId="+SPHelper.getString(Builds.SP_USER
                ,"sessionId")
                +"&userId="+useid;
    }


    public interface AddressSetListener{
        public void addressSetSuccess(MyAddressSetModel drugSetModel);
        public void addressSetError(long code, String msg);
    }
}