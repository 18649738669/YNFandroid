package com.yinaf.dragon.Content.Net;

import com.android.volley.Request;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by long on 2018/04/24.
 * 功能：照片上传临时云转正式云接口
 */

public class ImageUploadAPI extends DragonAPI {

    ImageUploadListener listener ;
    String imgUrl = "";

    public ImageUploadAPI(ImageUploadListener listener, String imgUrl ) {
        // TODO Auto-generated constructor stub
        super();
        this.listener = listener;
        this.imgUrl = imgUrl;

        LogUtils.d("[image_load-params]" + imgUrl);

        new DragonHttp(this).request();
    }


    @Override
    public void requestSuccess(JSONObject data) {
        // TODO Auto-generated method stub
        JSONArray content = JSONUtils.getJSONArray(data,"content");
        listener.imageUploadSuccess(content);
    }

    @Override
    public void requestError(long code, String msg) {
        // TODO Auto-generated method stub

        listener.imageUploadError(code, msg);
    }

    @Override
    public int getHttpType() {
        // TODO Auto-generated method stub
        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return Builds.HOST + "/file/copyBucket"+"?imgUrl="+imgUrl;
    }


    public interface ImageUploadListener{
        public void imageUploadSuccess(JSONArray content);
        public void imageUploadError(long code, String msg);
    }
}