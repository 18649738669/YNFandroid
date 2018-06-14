package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：生成续费套餐订单接口
 */

public class AddPackageOrderAPI extends DragonAPI {

    AddPackageOrderListener listener ;

    public AddPackageOrderAPI(AddPackageOrderListener listener,int memberId, int goodsNum, int payWay) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("goodsNum", goodsNum);
        addParams("payWay", payWay);
        addParams("memberId", memberId);
        LogUtils.d("[add_PackageOrder-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        String content = JSONUtils.getString(data,"obj");
        listener.addPackageOrderSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addPackageOrderError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/addPackageOrder" ;
    }


    public interface AddPackageOrderListener{
        public void addPackageOrderSuccess(String content);
        public void addPackageOrderError(long code, String msg);
    }
}