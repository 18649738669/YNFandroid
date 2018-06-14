package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Utils.WebViewSetting;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/08.
 * 功能：使用网页显示常见问题页面
 */

public class HelpAct extends BasicAct{


    @BindView(R.id.help_webview)
    WebView webview;

    String url = Builds.HOST + "/page/help/helpPage.html";
    LoadingDialog loadingDialog;

    public HelpAct() {
        super(R.layout.act_help, R.string.title_activity_help, false, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpAct.class);
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
        webview.goBack();
        //设置Web视图
        webview.setWebViewClient(new WebViewClient(){
            @Override
            // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 判断url链接中是否含有某个字段，如果有就执行指定的跳转（不执行跳转url链接），如果没有就加载url链接
                if (url.contains("goback")) {
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
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

}
