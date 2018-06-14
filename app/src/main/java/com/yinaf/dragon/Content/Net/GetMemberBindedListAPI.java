package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.LoginAct;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-5-10.
 * 功能：获取用户已绑定腕表成员及最后消息时间接口
 */

public class GetMemberBindedListAPI extends DragonAPI {

    public GetMemberBindedListListener listener;
    public String memberId;
    public String sessionId;
    public String day;
    public GetMemberBindedListAPI(GetMemberBindedListListener listener){
        this.listener = listener;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetBloodSugarToWeek-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }


    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/getMemberBindedList"+"?sessionId="+sessionId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        JSONArray content = JSONUtils.getJSONArray(data, "obj");
        listener.getMemberBindedListSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {
//
//        if (code == 5107){
//            ActivityCollector.finishAll();
//            LoginAct.startNewLoginAct();
//            TipUtils.showTip(msg);
//        }
        listener.getMemberBindedListError(code,msg);

    }

    public interface GetMemberBindedListListener{
        public void getMemberBindedListSuccess(JSONArray content);
        public void getMemberBindedListError(long code, String msg);
    }
}
