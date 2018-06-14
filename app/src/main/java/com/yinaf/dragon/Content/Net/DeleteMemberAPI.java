package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Net.BasicAPI;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018/04/24.
 * 功能：删除成员
 */

public class DeleteMemberAPI extends DragonAPI{

    DeleteMemberListener listener;

    public DeleteMemberAPI(DeleteMemberListener listener, String memberId){
        this.listener = listener;
        addParams("memberId", memberId);
        LogUtils.d("[delete_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();

    }

    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/user/delMemberOrUser" ;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = new JSONArray();
        listener.deleteMemberSuccess(content);

    }

    @Override
    public void requestError(long code, String msg) {
        listener.deleteMemberError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    public interface DeleteMemberListener{
        public void deleteMemberSuccess(JSONArray content);
        public void deleteMemberError(long code, String msg);
    }
}
