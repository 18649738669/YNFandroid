package com.yinaf.dragon.Content.Net;


import com.android.volley.Request;
import com.yinaf.dragon.Content.Activity.NewsDetailsAct;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Bean.NewsDetailsModel;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONObject;

public class NewsDetailsAPI  extends DragonAPI {

    private  String memberId;
    NewsDetailsListener listener;
    public String sessionId;
    public NewsDetailsAPI(NewsDetailsListener listener,String id){

        this.listener = listener;
        this.memberId = id;
        this.sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        new DragonBasicHttp(this).request();
    }




    @Override
    public String getUrl() {
        return Builds.HOST + "/mobile/index/getNews"+"?sessionId="+SPHelper.getString(Builds.SP_USER,"sessionId")+"&newId="+memberId;
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public void requestSuccess(JSONObject data) throws Exception {

        NewsDetailsModel model = JSONUtils.parseJson(data.toString(), NewsDetailsModel.class);
        listener.newsDetailsSuccess(model);

    }

    @Override
    public void requestError(long code, String msg) {

        listener.newsDetailsError(code,msg);
    }

    public interface NewsDetailsListener{
        public void newsDetailsSuccess(NewsDetailsModel content);
        public void newsDetailsError(long code,String msg);
    }
}
