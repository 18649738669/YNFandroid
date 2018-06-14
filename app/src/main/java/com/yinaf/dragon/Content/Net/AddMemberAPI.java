package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：新增成员接口
 */

public class AddMemberAPI extends DragonAPI {

    AddMemberListener listener ;

    public AddMemberAPI(AddMemberListener listener, int sex, String birthday, String image,
                        String blood, String rela, String height, String weight, String realName) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;

        addParams("image", image);
        addParams("sex", sex);
        addParams("birthday", birthday);
        addParams("blood", blood);
        addParams("rela", rela);
        addParams("height", height);
        addParams("weight", weight);
        addParams("realName", realName);
        LogUtils.d("[add_member-params]" + gson.toJson(params));
        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        JSONArray content = new JSONArray();
        listener.addMemberSuccess(content);
        Log.e("add_member-content",data.toString());
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.addMemberError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/mobile/user/addMember" ;
    }


    public interface AddMemberListener{
        public void addMemberSuccess(JSONArray content);
        public void addMemberError(long code, String msg);
    }
}