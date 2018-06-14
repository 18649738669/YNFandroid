package com.yinaf.dragon.Content.Service;

import android.app.IntentService;
import android.content.Intent;

import com.yinaf.dragon.Content.Receiver.DownloadReceiver;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Utils.FileUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by long on 2018-5-8.
 * 功能：下载更新的APK的后台服务
 */
public class DownloadAPKService extends IntentService {

    public DownloadAPKService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");

        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            File file = FileUtils.createFile(Builds.APK_PATH,"颐纳福.apk");
            if (file.exists()){
                file.delete();
            }
            OutputStream output = new FileOutputStream(file.getAbsolutePath());
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                int progress = (int) (total * 100 / fileLength);
                DownloadReceiver.send(this,progress);
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        DownloadReceiver.send(this,100);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("结束下载apk服务");
    }
}