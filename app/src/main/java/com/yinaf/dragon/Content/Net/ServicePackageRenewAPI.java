package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：续费1元标准套餐接口
 */

public class ServicePackageRenewAPI extends DragonAPI {

    ServicePackageRenewListener listener ;

    public ServicePackageRenewAPI(ServicePackageRenewListener listener,int memberId, int goodsNum) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;
        addParams("memberId", memberId);
        addParams("goodsNum", goodsNum);
        LogUtils.d("[ServicePackageRenew-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        listener.servicePackageRenewSuccess(data);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.servicePackageRenewError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/healthy/servicePackageRenew" ;
    }


    public interface ServicePackageRenewListener{
        public void servicePackageRenewSuccess(JSONObject content);
        public void servicePackageRenewError(long code, String msg);
    }
}