package com.yinaf.dragon.Content.Activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * web 跳转
 */
public class WebViewAct extends BasicAct {

    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.web_view)
    WebView webView;

    public WebViewAct() {
        super(R.layout.web_view_act, R.string.my_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        String title = getIntent().getStringExtra("title");
        toolBarFriendsCircleTitle.setText(title);

        webView.loadUrl(getIntent().getStringExtra("url"));
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }


}
