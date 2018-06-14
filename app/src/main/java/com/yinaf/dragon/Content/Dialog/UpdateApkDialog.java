package com.yinaf.dragon.Content.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yinaf.dragon.Content.Service.DownloadAPKService;
import com.yinaf.dragon.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018-5-8.
 * 功能：弹出框，提示用户是否更新最新版本的apk
 */
public class UpdateApkDialog extends Activity {

    @BindView(R.id.dialog_update_apk_tv_tip_detail)
    TextView update_tv;
    @BindView(R.id.dialog_update_apk_btn_download)
    Button btn_download;
    @BindView(R.id.dialog_update_apk_btn_cancel)
    Button dialogUpdateApkBtnCancel;

    private String updateTip = "";
    private String updateUrl = "";
    private String updateVersion = "";
    private int lastForce = -1;

    public static void startActivity(Context context, String updateTip, String updateUrl, String updateVersion, int lastForce) {
        Intent intent = new Intent(context, UpdateApkDialog.class);
        intent.putExtra("updateTip", updateTip);
        intent.putExtra("updateUrl", updateUrl);
        intent.putExtra("updateVersion", updateVersion);
        intent.putExtra("lastForce", lastForce);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_apk);
        ButterKnife.bind(this);
        updateTip = getIntent().getStringExtra("updateTip");
        updateUrl = getIntent().getStringExtra("updateUrl");
        updateVersion = getIntent().getStringExtra("updateVersion");
        lastForce = getIntent().getIntExtra("lastForce",0);
        update_tv.setText(updateTip);
        if (lastForce == 0){
            dialogUpdateApkBtnCancel.setVisibility(View.VISIBLE);
        }else {
            dialogUpdateApkBtnCancel.setVisibility(View.GONE);
        }
    }

    /**
     * 取消下载
     */
    @OnClick(R.id.dialog_update_apk_btn_cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.dialog_update_apk_btn_download)
    public void download() {

        btn_download.setEnabled(false);
        if (!TextUtils.isEmpty(updateUrl)) {
            Intent downloadIntent = new Intent(this, DownloadAPKService.class);
            downloadIntent.putExtra("url", updateUrl);
            startService(downloadIntent);
            DownloadApkDialog.startActivity(this, updateUrl,lastForce);
        }
        finish();

    }
}
