package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by long on 2018-5-10.
 * 功能：获取聊天记录接口
 */

public class GetMemberMessagesAPI extends DragonAPI {

    public GetMemberMessagesListener listener;
    public int memberId;
    public String sessionId;
    public int mId;
    public GetMemberMessagesAPI(GetMemberMessagesListener listener, int mId, int memberId){
        this.listener = listener;
        this.memberId = memberId;
        this.mId = mId;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        LogUtils.d("[GetMemberMessages-params]" + gson.toJson(params));
        new DragonBasicHttp(this).request();
    }

    @Override
    public String getUrl() {
        if (mId > 0) {
            return Builds.HOST + "/mobile/user/getMemberMessages" + "?memberId=" + memberId + "&mId=" + mId + "&sessionId=" + sessionId;
        }else {
            return Builds.HOST + "/mobile/user/getMemberMessages" + "?memberId=" + memberId  + "&sessionId=" + sessionId;
        }
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {
        JSONArray content = JSONUtils.getJSONArray(data,"obj");
        listener.getMemberMessagesSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {

        listener.getMemberMessagesError(code,msg);

    }

    public interface GetMemberMessagesListener{
        public void getMemberMessagesSuccess(JSONArray content) throws JSONException;
        public void getMemberMessagesError(long code, String msg);
    }
}
