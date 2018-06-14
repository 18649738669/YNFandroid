package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.VolleyError;
import com.yinaf.dragon.Content.Activity.LoginAct;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.Tool.Net.BasicHttp;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by long on 2018-4-12.
 */
public class DragonBasicHttp extends BasicHttp {

    private DragonAPI basicAPI;

    public DragonBasicHttp(DragonAPI basicAPI){
        super(basicAPI);
        this.basicAPI = basicAPI;
    }

    @Override
    public void onResponse(JSONObject response) {
        LogUtils.d("response-->" + response);
		try{
            int code = JSONUtils.getInt(response, "code");
            boolean success = JSONUtils.getBoolean(response, "success");
            String msg = JSONUtils.getString(response,"msg");
            JSONArray content = JSONUtils.getJSONArray(response,"content");
            if (code == 2000 || success){
                basicAPI.requestSuccess(response);
            }else{
                if (code == 5107){
                    ActivityCollector.finishAll();
                    LoginAct.startNewLoginAct();
                    TipUtils.showTip(msg);
                }
                basicAPI.requestError(code,msg);
            }
        }catch (Exception e){
            e.printStackTrace();
            basicAPI.requestError(0, "服务器繁忙，请稍后再试1");
        }

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("volleyError----->",volleyError.toString());

        basicAPI.requestError(0, "服务器繁忙，请稍后再试2");
    }
}
