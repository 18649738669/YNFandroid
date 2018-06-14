package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetHealthRecordAPI;
import com.yinaf.dragon.Content.Utils.WebViewSetting;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/08.
 * 功能：使用网页显示健康报告月度统计
 */

public class HealthReportWebViewAct extends BasicAct{


    @BindView(R.id.health_report_webview)
    WebView webview;

    String url = Builds.HOST + "/page/health_report/dist/index.html";
//    String url = "http://192.168.1.128:9000/#/";
//    String url = "http://192.168.1.128:3000/#/";
    LoadingDialog loadingDialog;

    public HealthReportWebViewAct() {
        super(R.layout.act_health_report_webview, R.string.title_activity_health_report, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId, String month) {
        Intent intent = new Intent(context, HealthReportWebViewAct.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("month", month);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        loadingDialog.show();
        initWebView();
    }

    /**
     * 初始化WebView
     */
    public void initWebView(){
        webview.requestFocus();//请求获得焦点
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        new WebViewSetting().setSetting(webview);
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient (this,webview,getIntent().getStringExtra("memberId"),getIntent().getStringExtra("month")));
        loadingDialog.dismiss();
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
    }

    /**
     *     Web视图
     */
    private class webViewClient extends WebViewClient {

        private Context context;
        private WebView webView;
        private String memberId;
        private String month;

        public webViewClient(Context context, WebView webView, String memberId,String month){
            this.context=context;
            this.webView=webView;
            this.memberId=memberId;
            this.month=month;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {//当页面加载完成
            super.onPageFinished(view, url);
            callMethod(webView);
        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * 调用js方法  传入值
         *
         * @param webView
         */
        public void callMethod(WebView webView) {
            Log.e("实测",memberId +"  " + month + "  " + SPHelper.getString(Builds.SP_USER,"sessionId"));
            String call = "javascript:memberId(\"" + memberId + "\")";
            webView.loadUrl(call);

            call = "javascript:month(\"" + month + "\")";
            webView.loadUrl(call);

            call = "javascript:sessionId(\"" + SPHelper.getString(Builds.SP_USER,"sessionId") + "\")";
            webView.loadUrl(call);
        }
    }
}
