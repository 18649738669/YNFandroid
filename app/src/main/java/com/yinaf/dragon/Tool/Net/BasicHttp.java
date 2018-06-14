package com.yinaf.dragon.Tool.Net;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.yinaf.dragon.Tool.APP.App;


public abstract class BasicHttp implements Response.Listener<JSONObject>,Response.ErrorListener{
	
	public BasicAPI basicAPI;
	
	public BasicRequest basicRequest;
	
	public BasicHttp(BasicAPI basicAPI){
		this.basicAPI = basicAPI;
    }
	
	public void request(){
		SsX509TrustManager.allowAllSSL();   //允许Https协议

		basicRequest = new BasicRequest(basicAPI, this, this);

		basicRequest.setRetryPolicy(new DefaultRetryPolicy(80 * 1000,0,DefaultRetryPolicy
				.DEFAULT_BACKOFF_MULT));

		App.getRequestQueue().add(basicRequest);

	}


	public void setTag(Object tag){
        if (tag != null){
            basicRequest.setTag(tag);
        }
	}

}
