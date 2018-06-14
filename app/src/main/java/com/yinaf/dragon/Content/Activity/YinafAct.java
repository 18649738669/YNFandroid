package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Receiver.RefreshPayReceiver;
import com.yinaf.dragon.Content.Utils.AuthResult;
import com.yinaf.dragon.Content.Utils.PayResult;
import com.yinaf.dragon.Content.Utils.WebViewSetting;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import wendu.webviewjavascriptbridge.WVJBWebView;

/**
 * Created by long on 2018/05/08.
 * 功能：颐纳福商城页面
 */

public class YinafAct extends BasicAct implements RefreshPayReceiver.RefreshPayListener{


    @BindView(R.id.yinaf_webview)
    WVJBWebView webview;

    String url = "http://mall.yinaf.com/pages";
//    String url = "http://www.javatest.top";
    LoadingDialog loadingDialog;
    String userId;
    String sessionId;
    webViewClient webViewClient;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private IWXAPI api;
    RefreshPayReceiver refreshPayReceiver;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (resultStatus.equals("9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(YinafAct.this, "支付成功", Toast.LENGTH_SHORT).show();
                        webViewClient.payResults("success");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(YinafAct.this, "支付失败", Toast.LENGTH_SHORT).show();
                        webViewClient.payResults("failure");
                    }
                    break;
                case SDK_AUTH_FLAG:
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (resultStatus.equals("9000") && authResult.getResultCode().equals( "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        TipUtils.showTip("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        TipUtils.showTip("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                default:
                    break;
            }
        };
    };

    public YinafAct() {
        super(R.layout.act_yinaf, R.string.title_activity_yinaf, false, TOOLBAR_TYPE_FULL_SCREEN, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, YinafAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        refreshPayReceiver = new RefreshPayReceiver(this);
        RefreshPayReceiver.register(this,refreshPayReceiver);
        api = WXAPIFactory.createWXAPI(this, "wx7e6fb6e371626009");
        loadingDialog = LoadingDialog.showDialog(this);
        loadingDialog.show();
        userId = SPHelper.getString(Builds.SP_USER,"userId");
        sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        initWebView();

    }

    /**
     * 初始化WebView
     */
    public void initWebView() {
        new WebViewSetting().setSetting(webview);
        webViewClient = new webViewClient(this,webview);
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(url);
        webViewClient.goToPay();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清楚webview缓存
        webview.clearCache(true);
        RefreshPayReceiver.unregister(this,refreshPayReceiver);
    }

    /**
     * 接收到微信支付结果的广播
     * @param code
     */
    @Override
    public void receiverRefreshPay(int code) {

        if (code == -1){
            webViewClient.payResults("failure");
        }else if (code == 0){
            webViewClient.payResults("success");
        }else if (code == -2){
            webViewClient.payResults("cancellation");
        }else {
            webViewClient.payResults("handing");
        }

    }

    /**
     *     Web视图
     */
    private class webViewClient extends WebViewClient {

        private Context context;
        private WebView webView;

        public webViewClient(Context context, WebView webView){
            this.context=context;
            this.webView=webView;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {//当页面加载完成
            super.onPageFinished(view, url);
            callMethod();
            loadingDialog.dismiss();

        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 判断url链接中是否含有某个字段，如果有就执行指定的跳转（不执行跳转url链接），如果没有就加载url链接
            if (url.contains("goback")) {
                finish();
                return true;
            }else if (url.contains("foundwechat")){//拦截微信检查
                if (wxCanPay()){
                    api.registerApp("wx7e6fb6e371626009");
                    callWx(0);
                }else {
                    callWx(-1);
                }
                return true;
            }
            else if (url.contains("jumpwechat")){//拦截微信
                return true;
            }else if (url.contains("jumpzfb")){//拦截支付宝
                return true;
            }
            else {
                return false;
            }
        }

        /**
         * 调用js方法  传入值
         *
         */
        public void callMethod() {
            try {
                JSONObject data = new JSONObject();
                data.put("sessionId",sessionId);
                data.put("userId",userId);
                webview.callHandler("sessionId",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 查询是否存在微信
         * isWx  -1 不存在微信，0 存在微信正常支付
         */
        public void callWx(int isWx) {
            try {
                JSONObject data = new JSONObject();
                if (isWx == -1){
                    data.put("wechat","nofound");
                }else if (isWx == 0){
                    data.put("wechat","found");
                }
                webview.callHandler("foundwechat", data, new WVJBWebView.WVJBResponseCallback() {
                    @Override
                    public void onResult(Object data) {
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /**
         * 调用支付
         * 1 微信支付  2 支付宝支付
         */
        public void goToPay(){
                webview.registerHandler("goToPay", new WVJBWebView.WVJBHandler() {
                    @Override
                    public void handler(Object jsonObject, WVJBWebView.WVJBResponseCallback callback) {

                        callback.onResult(jsonObject.toString());
                        JSONObject jsonObject1 = null;
                        int pay = 0;
                        try {
                            pay = JSONUtils.getInt(new JSONObject(jsonObject.toString()),"payway");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (pay == 1) {//微信支付
                            try {
                                jsonObject1 = JSONUtils.getJSONObject(new JSONObject(jsonObject.toString()),"obj");
                                PayReq payReq = new PayReq();
                                payReq.appId = JSONUtils.getString(jsonObject1,"appid");
                                payReq.partnerId = JSONUtils.getString(jsonObject1,"partnerid");
                                payReq.prepayId = JSONUtils.getString(jsonObject1,"prepayid");
                                payReq.packageValue = "Sign=WXPay";
                                payReq.nonceStr = JSONUtils.getString(jsonObject1,"noncestr");
                                payReq.timeStamp = JSONUtils.getString(jsonObject1,"timestamp");
                                payReq.sign = JSONUtils.getString(jsonObject1,"sign");
                                api.sendReq(payReq);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                        }else if (pay == 2){//支付宝
                            try {
                                jsonObject1 = new JSONObject(jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final String orderInfo = JSONUtils.getString(jsonObject1,"obj");   // 订单信息

                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(YinafAct.this);
                                    Map<String, String> result = alipay.payV2(orderInfo,true);

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                            loadingDialog.dismiss();
                        }
                    }
                });
        }

        public void payResults(String results){
            try {
                JSONObject data = new JSONObject();
                data.put("zfbpayresult",results);
                webview.callHandler("zfbpayresult",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 检测微信
     * @return
     */
    private boolean wxCanPay(){

        try {
            if (!api.isWXAppInstalled()){
                TipUtils.showTip("请安装微信！");
                return false;
            }
            if (!api.isWXAppSupportAPI()){
                TipUtils.showTip("微信版本不支持支付！");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            TipUtils.showTip("请安装或者升级微信版本！");
            return false;
        }
        return true;
    }
}
