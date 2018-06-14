package com.yinaf.dragon.Content.Net;

import android.util.Log;

import com.android.volley.VolleyError;
import com.yinaf.dragon.Content.Activity.LoginAct;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.Tool.Net.BasicHttp;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

/**
 * Created by long on 2018-4-12.
 */
public class DragonHttp extends BasicHttp {

    private DragonAPI basicAPI;

    public DragonHttp(DragonAPI basicAPI){
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
            if (code == 2000 || success){
                basicAPI.requestSuccess(response);
            }else if (code == 5107) {

                TipUtils.showTip("登录超时，请重新登录");
                ActivityCollector.finishAll();
                LoginAct.startNewLoginAct();

            }else {

                basicAPI.requestError(code,msg);
            }
        }catch (Exception e){
            LogUtils.d(Log.getStackTraceString(e));
            basicAPI.requestError(0, "解析出错");
        }

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        basicAPI.requestError(0, "服务器繁忙，请稍后再试3");
    }
}
