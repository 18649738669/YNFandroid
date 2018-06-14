package com.yinaf.dragon.Content.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yinaf.dragon.Content.Receiver.DownloadReceiver;
import com.yinaf.dragon.Content.Service.DownloadAPKService;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.ResUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018-5-8.
 * 功能：弹出框，提示用户是否更新最新版本的apk
 */
public class DownloadApkDialog extends Activity implements DownloadReceiver.DownloadListener {


    @BindView(R.id.dialog_download_apk_tv)
    TextView tv_title;
    @BindView(R.id.dialog_download_apk_progress)
    ProgressBar progressBar;
    @BindView(R.id.dialog_download_apk_tv_update)
    TextView tv_download;
    @BindView(R.id.dialog_download_apk_btn_cancel)
    Button dialogDownloadApkBtnCancel;

    private String updateVersion;
    private DownloadReceiver downloadReceiver;
    private int currentProgress = 0;
    private int lastForce = -1;


    public static void startActivity(Context context, String updateVersion,int lastForce) {
        Intent intent = new Intent(context, DownloadApkDialog.class);
        intent.putExtra("updateVersion", updateVersion);
        intent.putExtra("lastForce", lastForce);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_apk);
        ButterKnife.bind(this);

        downloadReceiver = new DownloadReceiver(this);
        DownloadReceiver.register(this, downloadReceiver);

//        updateVersion = getIntent().getStringExtra("updateVersion");
        updateVersion = "";
        lastForce = getIntent().getIntExtra("lastForce",0);
        tv_title.setText(ResUtils.getString(R.string.dialog_download_apk_tv, updateVersion));
        if (lastForce == 0){
            dialogDownloadApkBtnCancel.setVisibility(View.VISIBLE);
        }else {
            dialogDownloadApkBtnCancel.setVisibility(View.GONE);
        }

    }

    /**
     * 取消下载
     */
    @OnClick(R.id.dialog_download_apk_btn_cancel)
    public void downloadCancel() {
        stopService(new Intent(this, DownloadAPKService.class));
        finish();
    }


    @Override
    public void receivedDownload(int progress) {
        if (progress != currentProgress) {
            progressBar.setProgress(progress);
            tv_download.setText(progress + " %");
            currentProgress = progress;
        }

        if (progress == 100) {
            //下载完成
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Builds.APK_PATH, "颐纳福.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        DownloadReceiver.unregister(this, downloadReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
